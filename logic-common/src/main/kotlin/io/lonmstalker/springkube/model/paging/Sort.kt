package io.lonmstalker.springkube.model.paging

data class Sort(
    val field: String,
    val type: SortType,
    val order: Int = 0
)
