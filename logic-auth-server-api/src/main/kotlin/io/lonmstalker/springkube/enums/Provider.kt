package io.lonmstalker.springkube.enums

enum class Provider(val grantType: String, val principal: String, val credentials: String) {
    PASSWORD("password", "username", "password"),
    CLIENT_CREDENTIALS("client_credentials", "client_id", "client_secret"),
    AUTHORIZATION_CODE("authorization_code", "username", "password"),
    REFRESH_TOKEN("refresh_token", "state", "code");
}

val providerByGrantType: Map<String, Provider> = Provider.values().associateBy { it.grantType }