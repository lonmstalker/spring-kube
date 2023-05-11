package io.lonmstalker.springkube.helper

import io.lonmstalker.springkube.exception.SystemObjectNotFoundException
import io.lonmstalker.springkube.model.paging.Filter
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.Operation
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import java.time.OffsetDateTime

class JooqHelper(private val ctx: DSLContext) {

    fun select(table: Table<*>, rq: FilterRequest) =
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
            Operation.OR -> this.or(table, filter)
            Operation.AND -> this.and(table, filter)
        }

    private fun or(table: Table<*>, filter: Filter): Condition =
        filter.value
            ?.map { getField(table, it as Filter) }
            ?.reduce { acc, condition -> acc.or(condition) }
            ?: throw SystemObjectNotFoundException(filter.field)

    private fun and(table: Table<*>, filter: Filter): Condition =
        filter.value
            ?.map { getField(table, it as Filter) }
            ?.reduce { acc, condition -> acc.and(condition) }
            ?: throw SystemObjectNotFoundException(filter.field)

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