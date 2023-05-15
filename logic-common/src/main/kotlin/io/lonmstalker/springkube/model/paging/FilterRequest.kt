package io.lonmstalker.springkube.model.paging

data class FilterRequest(
    val paging: Paging?,
    val sort: List<Sort>?,
    val filters: List<Filter>?
)
