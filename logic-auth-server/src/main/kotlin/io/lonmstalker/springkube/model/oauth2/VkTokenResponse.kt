package io.lonmstalker.springkube.model.oauth2

import com.fasterxml.jackson.annotation.JsonProperty

data class VkTokenResponse(

    @JsonProperty("access_token")
    val accessToken: String,

    @JsonProperty("expires_in")
    val expiresIn: Long,

    @JsonProperty("user_id")
    val userId: String,

    val email: String
)