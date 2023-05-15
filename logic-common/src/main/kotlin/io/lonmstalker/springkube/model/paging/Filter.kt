package io.lonmstalker.springkube.model.paging

data class Filter(
    val field: String,
    val value: List<Any>?,
    val operation: Operation,
)
