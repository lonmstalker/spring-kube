package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.config.MapstructConfig
import io.lonmstalker.springkube.dto.FilterDto
import io.lonmstalker.springkube.dto.FilterRequestDto
import io.lonmstalker.springkube.dto.PagingResponseDto
import io.lonmstalker.springkube.dto.SortDto
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.Operation
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.model.paging.SortType
import org.mapstruct.Mapper

@Mapper(config = MapstructConfig::class)
interface FilterMapper {

    fun toModel(filter: FilterRequestDto): FilterRequest

    fun toDto(paging: PageResponse): PagingResponseDto

    fun map(operation: FilterDto.Operation): Operation = Operation.VALUES_MAP[operation.name]!!

    fun map(sort: SortDto.Type): SortType = if (sort.name == SortType.ASC.value) SortType.ASC else SortType.DESC
}