package io.lonmstalker.springkube.helper

import io.lonmstalker.springkube.constants.CommonConstants.FIELD
import io.lonmstalker.springkube.constants.CommonConstants.OPERATION
import io.lonmstalker.springkube.constants.CommonConstants.VALUE
import io.lonmstalker.springkube.exception.DefaultException
import io.lonmstalker.springkube.exception.SystemObjectNotFoundException
import io.lonmstalker.springkube.model.paging.Filter
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.Operation
import io.lonmstalker.springkube.model.paging.PageResponse
import kotlinx.coroutines.reactive.awaitFirst
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import reactor.core.publisher.Flux
import java.time.OffsetDateTime

class JooqHelper(private val ctx: DSLContext) {

    suspend fun <T> selectFluxWithCount(
        table: Table<*>,
        rq: FilterRequest,
        transformation: (Record) -> T,
        additionalFilters: Array<Condition>?
    ): Pair<PageResponse, List<T>> {
        val filters = rq.filters?.mapTo(mutableListOf()) { getField(table, it) } ?: mutableListOf()

        if (additionalFilters != null) {
            filters.addAll(additionalFilters)
        }

        val count = ctx.selectCount().from(table).where(filters).awaitFirst().get(0, Int::class.java)
        val data = Flux.from(ctx.selectFrom(table)).collectList().awaitFirst().map(transformation)

        return PageResponse(page = if (data.isNotEmpty()) rq.paging?.page ?: 0 else 0, totalCount = count) to data
    }

    fun select(table: Table<*>, rq: FilterRequest): SelectConditionStep<out Record> =
        this.ctx
            .selectFrom(table)
            .where(rq.filters?.map { this.getField(table, it) })

    private fun getField(table: Table<*>, filter: Filter): Condition =
        when (filter.operation) {
            Operation.EQUALS -> table.getFieldVarchar(filter).run { this.eq(first(filter).coerce(this)) }
            Operation.CONTAINS -> table.getFieldVarchar(filter).run { this.contains(first(filter).coerce(this)) }
            Operation.IN -> table.getField(filter).run { this.`in`(filter.value) }
            Operation.NOT_IN -> table.getField(filter).run { this.notIn(filter.value) }
            Operation.BETWEEN -> this.between(table, filter)
            Operation.GTE -> table.getFieldInt(filter).run { this.ge(first(filter).coerce(this)) }
            Operation.GT -> table.getFieldInt(filter).run { this.gt(first(filter).coerce(this)) }
            Operation.LTE -> table.getFieldInt(filter).run { this.le(first(filter).coerce(this)) }
            Operation.LT -> table.getFieldInt(filter).run { this.lt(first(filter).coerce(this)) }
            Operation.NOT_EQUALS -> table.getFieldVarchar(filter).run { this.ne(first(filter).coerce(this)) }
            Operation.OR -> this.reduce(table, filter) { f, s -> f.or(s) }
            Operation.AND -> this.reduce(table, filter) { f, s -> f.and(s) }
        }

    private fun reduce(table: Table<*>, filter: Filter, reduce: (Condition, Condition) -> Condition): Condition =
        filter.value
            ?.map { getField(table, getFilterValue(it)) }
            ?.reduce(reduce)
            ?: throw SystemObjectNotFoundException(filter.field)

    private fun getFilterValue(value: Any): Filter {
        if (value is Filter) {
            return value
        }
        if (value is Map<*, *>) {
            return Filter(
                value = value[FIELD] as? List<Any>,
                field = value[VALUE] as String,
                operation = Operation.values().first { it.value == OPERATION }
            )
        }
        throw DefaultException("value is not filter")
    }

    private fun Table<*>.getFieldInt(filter: Filter): Field<Int> =
        this.getField(filter).coerce(SQLDataType.INTEGER)

    private fun Table<*>.getFieldVarchar(filter: Filter): Field<String> =
        this.getField(filter).coerce(SQLDataType.VARCHAR)

    private fun Table<*>.getField(filter: Filter): Field<*> =
        this.field(filter.field)
            ?: throw SystemObjectNotFoundException(filter.field)

    private fun first(filter: Filter): Param<*> = getByIndex(filter, 0)

    private fun between(table: Table<*>, filter: Filter): Condition {
        val field = table.getField(filter)
        val dataType = field.dataType.type
        if (OffsetDateTime::class.isInstance(dataType)) {
            return castBetween(field, filter, OffsetDateTime::class.java)
        }
        if (Int::class.isInstance(dataType)) {
            return castBetween(field, filter, Int::class.java)
        }
        throw SystemObjectNotFoundException(filter.field)
    }

    private fun <T> castBetween(field: Field<*>, filter: Filter, clazz: Class<T>) =
        field.cast(clazz)
            .between(
                first(filter).coerce(clazz),
                getByIndex(filter, 1).coerce(clazz)
            )

    private fun getByIndex(filter: Filter, index: Int): Param<*> =
        filter.value
            ?.get(index)
            ?.run { DSL.value(this) }
            ?: throw SystemObjectNotFoundException(filter.field)
}