package io.lonmstalker.springkube.model.paging

data class FilterRequest(
    val paging: Paging?,
    val sort: List<Sort>?,
    val filters: MutableList<Filter> = mutableListOf()
)
