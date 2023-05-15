package io.lonmstalker.springkube.model.oauth2

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkUserInfo(
    val response: List<VkUserInfoResponse>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkUserInfoResponse(
    val id: String,

    @JsonProperty("first_name")
    val firstName: String,

    @JsonProperty("last_name")
    val lastName: String
)