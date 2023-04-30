package io.lonmstalker.springkube.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class ErrorDto(
    @field:JsonProperty("code") val code: String,
    @field:JsonProperty("status") val status: Int,
    @field:JsonProperty("message") val message: String?,
    @field:JsonProperty("data") val data: List<FieldError>?,
    @field:JsonProperty("timestamp") val timestamp: LocalDateTime
)