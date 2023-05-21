package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.config.MapstructConfig
import io.lonmstalker.springkube.dto.*
import io.lonmstalker.springkube.model.paging.*
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(config = MapstructConfig::class)
interface FilterMapper {

    @Mapping(target = "paging", defaultExpression = "java(new Paging(0, 250))")
    fun toModel(filter: FilterRequestDto): FilterRequest

    fun toDto(paging: PageResponse): PagingResponseDto

    fun map(operation: FilterDto.Operation): Operation = Operation.VALUES_MAP[operation.name]!!

    fun map(sort: SortDto.Type): SortType = if (sort.name == SortType.ASC.value) SortType.ASC else SortType.DESC
}
