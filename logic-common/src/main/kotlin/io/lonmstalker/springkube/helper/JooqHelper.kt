package io.lonmstalker.springkube.helper

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import io.lonmstalker.springkube.constants.CommonConstants.CREATED_DATE_FIELD
import io.lonmstalker.springkube.constants.CommonConstants.DOT
import io.lonmstalker.springkube.constants.CommonConstants.FIELD
import io.lonmstalker.springkube.constants.CommonConstants.ID_FIELD
import io.lonmstalker.springkube.constants.CommonConstants.LEFT_BRACKET
import io.lonmstalker.springkube.constants.CommonConstants.OPERATION
import io.lonmstalker.springkube.constants.CommonConstants.VALUE
import io.lonmstalker.springkube.exception.DefaultException
import io.lonmstalker.springkube.exception.SystemObjectNotFoundException
import io.lonmstalker.springkube.model.paging.*
import io.lonmstalker.springkube.model.paging.Operation.Companion.VALUES_MAP
import io.lonmstalker.springkube.model.system.SelectData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactive.awaitFirst
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import reactor.core.publisher.Flux
import java.time.OffsetDateTime
import java.util.*

typealias TableCondition = Pair<Table<*>, Condition>

class JooqHelper(private val ctx: DSLContext, private val schema: Schema) {

    suspend fun <T> selectFluxWithCount(
        table: Table<*>,
        rq: FilterRequest,
        transformation: (Record) -> T
    ): Pair<PageResponse, List<T>> {
        val selectData = rq.createSelectData(table)
        return this.internalPageRequest(table, selectData, rq, transformation)
    }

    suspend fun <T> selectFluxWithCount(
        table: Table<*>,
        rq: FilterRequest,
        transformation: (Record) -> T,
        additionalFilter1: Condition
    ): Pair<PageResponse, List<T>> {
        val selectData = rq.createSelectData(table)
        selectData.conditions.add(additionalFilter1)
        return this.internalPageRequest(table, selectData, rq, transformation)
    }

    suspend fun <T> selectFluxWithCount(
        table: Table<*>,
        rq: FilterRequest,
        transformation: (Record) -> T,
        additionalFilter1: Condition,
        additionalFilter2: Condition
    ): Pair<PageResponse, List<T>> {
        val selectData = rq.createSelectData(table)
        selectData.conditions.add(additionalFilter1)
        selectData.conditions.add(additionalFilter2)
        return this.internalPageRequest(table, selectData, rq, transformation)
    }

    suspend fun <T> selectFluxWithCountManyFilters(
        table: Table<*>,
        rq: FilterRequest,
        transformation: (Record) -> T,
        vararg additionalFilter: Condition
    ): Pair<PageResponse, List<T>> {
        val selectData = rq.createSelectData(table)
        selectData.conditions.addAll(additionalFilter)
        return this.internalPageRequest(table, selectData, rq, transformation)
    }

    fun select(table: Table<*>, rq: FilterRequest): SelectConditionStep<out Record> {
        val selectData = rq.createSelectData(table)
        return this.ctx
            .select()
            .from(table)
            .addJoin(selectData.neededJoin)
            .where(selectData.conditions)
    }

    private fun FilterRequest.createSelectData(table: Table<*>): SelectData {
        val neededJoin = mutableMapOf<String, TableCondition>()
        val conditions = this.getConditions(table, neededJoin)
        return SelectData(conditions, neededJoin.values)
    }

    private fun FilterRequest.getConditions(table: Table<*>, neededJoin: MutableMap<String, TableCondition>) =
        this.filters?.mapTo(mutableListOf()) { getField(table, it, neededJoin) } ?: mutableListOf()

    private fun FilterRequest.getSort(table: Table<*>): List<SortField<out Any>> =
        this.sort
            ?.sortedBy { it.order }
            ?.map {
                table.field(SnakeCaseStrategy.INSTANCE.translate(it.field))!!.sort(SortOrder.valueOf(it.type.name))
            }
            ?: listOf(table.field(CREATED_DATE_FIELD)!!.sort(SortOrder.DESC))

    private suspend fun <T> internalPageRequest(
        table: Table<*>,
        selectData: SelectData,
        rq: FilterRequest,
        transformation: (Record) -> T
    ): Pair<PageResponse, List<T>> =
        coroutineScope {
            val count = async { selectCount(table, selectData) }
            val data = async { selectData(table, selectData, rq, transformation) }.await()
            PageResponse(
                page = if (data.isNotEmpty()) rq.paging.page else 0,
                totalCount = count.await()
            ) to data
        }

    private suspend fun <T> selectData(
        table: Table<*>,
        selectData: SelectData,
        rq: FilterRequest,
        transformation: (Record) -> T
    ) = ctx.select()
        .from(table)
        .addJoin(selectData.neededJoin)
        .where(selectData.conditions)
        .orderBy(rq.getSort(table))
        .limit(rq.paging.pageSize)
        .offset(rq.paging.pageSize * rq.paging.page)
        .run { Flux.from(this) }
        .collectList()
        .awaitFirst()
        .map(transformation)

    private suspend fun selectCount(table: Table<*>, selectData: SelectData) =
        ctx.selectCount()
            .from(table)
            .addJoin(selectData.neededJoin)
            .where(selectData.conditions)
            .awaitFirst()
            .get(0, Int::class.java)

    private fun SelectJoinStep<*>.addJoin(join: Collection<TableCondition>?): SelectJoinStep<*> {
        if (join.isNullOrEmpty()) {
            return this
        }
        var joined = join.first().let { this.leftJoin(it.first).on(it.second) }
        join.forEach { joined = this.join(it.first).on(it.second) }
        return joined
    }

    private fun getField(table: Table<*>, filter: Filter, neededJoin: MutableMap<String, TableCondition>?): Condition =
        when (filter.operation) {
            Operation.EQUALS -> table.getFieldVarchar(filter.getField(table, neededJoin))
                .run { this.eq(first(filter).coerce(this)) }

            Operation.CONTAINS -> table.getFieldVarchar(filter.getField(table, neededJoin))
                .run { this.contains(first(filter).coerce(this)) }

            Operation.IN -> table.getField(filter.getField(table, neededJoin)).run { this.`in`(filter.value) }
            Operation.NOT_IN -> table.getField(filter.getField(table, neededJoin))
                .run { this.notIn(filter.value) }

            Operation.BETWEEN -> this.between(table, filter)
            Operation.GTE -> table.getFieldInt(filter.getField(table, neededJoin))
                .run { this.ge(first(filter).coerce(this)) }

            Operation.GT -> table.getFieldInt(filter.getField(table, neededJoin))
                .run { this.gt(first(filter).coerce(this)) }

            Operation.LTE -> table.getFieldInt(filter.getField(table, neededJoin))
                .run { this.le(first(filter).coerce(this)) }

            Operation.LT -> table.getFieldInt(filter.getField(table, neededJoin))
                .run { this.lt(first(filter).coerce(this)) }

            Operation.NOT_EQUALS -> table.getFieldVarchar(filter.getField(table, neededJoin))
                .run { this.ne(first(filter).coerce(this)) }

            Operation.OR -> this.reduce(table, filter) { f, s -> f.or(s) }
            Operation.AND -> this.reduce(table, filter) { f, s -> f.and(s) }
        }

    private fun Filter.getField(table: Table<*>, neededJoin: MutableMap<String, TableCondition>?): String {
        val splitField = this.field!!.split(DOT)
        return if (splitField.size == 1) {
            splitField[0]
        } else {
            val splitBracket = splitField[0].split(LEFT_BRACKET)
            if (neededJoin?.containsKey(this.field) == true) {
                return splitBracket[0].dropLast(1)
            }
            if (neededJoin != null) {
                if (splitBracket.size == 1) {
                    throw DefaultException("table not found in field: " + this.field)
                }
                val referenceTable =
                    table.references.firstOrNull { it.key.fields.any { it.name == splitField[0] } }?.table?.asTable()
                        ?: throw DefaultException("table not exists: " + this.field)
                val joinCondition =
                    table.field(SnakeCaseStrategy.INSTANCE.translate(splitField[0]))!!.coerce(UUID::class.java)
                        .eq(referenceTable.field(ID_FIELD)!!.coerce(UUID::class.java))
                neededJoin[this.field] = referenceTable to joinCondition
                splitBracket[0]
            }
            splitBracket[0]
        }
    }

    private fun reduce(table: Table<*>, filter: Filter, reduce: (Condition, Condition) -> Condition): Condition =
        filter.value
            ?.map { getField(table, getFilterValue(it), null) }
            ?.reduce(reduce)
            ?: throw SystemObjectNotFoundException(filter.field ?: "")

    @Suppress("UNCHECKED_CAST")
    private fun getFilterValue(value: Any): Filter {
        if (value is Filter) {
            return value
        }
        if (value is Map<*, *>) {
            return Filter(
                field = value[FIELD] as? String,
                value = value[VALUE] as? List<Any>,
                operation = VALUES_MAP[value[OPERATION]]!!
            )
        }
        throw DefaultException("value is not filter")
    }

    private fun Table<*>.getFieldInt(field: String): Field<Int> =
        this.getField(field).coerce(SQLDataType.INTEGER)

    private fun Table<*>.getFieldVarchar(field: String): Field<String> =
        this.getField(field).coerce(SQLDataType.VARCHAR)

    private fun Table<*>.getField(field: String): Field<*> =
        this.field(SnakeCaseStrategy.INSTANCE.translate(field))
            ?: throw SystemObjectNotFoundException(field)

    private fun first(filter: Filter): Param<*> = getByIndex(filter, 0)

    private fun between(table: Table<*>, filter: Filter): Condition {
        val field = table.getField(filter.field!!)
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
        field.coerce(clazz)
            .between(
                first(filter).coerce(clazz),
                getByIndex(filter, 1).coerce(clazz)
            )

    private fun getByIndex(filter: Filter, index: Int): Param<*> =
        filter.value
            ?.get(index)
            ?.run { DSL.value(this) }
            ?: throw SystemObjectNotFoundException(filter.field ?: "")
}