package io.lonmstalker.springkube.enums

import io.lonmstalker.springkube.constants.ErrorConstants
import io.lonmstalker.springkube.exception.AuthException

enum class Provider(val grantType: String, val principal: String, val credentials: String) {
    PASSWORD("password", "username", "password"),
    CLIENT_CREDENTIALS("client_credentials", "client_id", "client_secret"),
    AUTHORIZATION_CODE("authorization_code", "username", "password"),
    REFRESH_TOKEN("refresh_token", "state", "code");
}

private val providerByGrantType: Map<String, Provider> = Provider.values().associateBy { it.grantType }

fun providerByGrant(value: String): Provider = providerByGrantType[value] ?: throw exceptionUnknownGrantType()

fun exceptionUnknownGrantType() = AuthException(ErrorConstants.OAUTH2_GRANT_TYPE_UNKNOWN, "grant_type not found")