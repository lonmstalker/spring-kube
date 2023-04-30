package io.lonmstalker.springkube.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class FieldError(
    @field:JsonProperty("code") val code: String,
    @field:JsonProperty("field") val field: String,
    @field:JsonProperty("message") val message: String?,
)