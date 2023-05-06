package io.lonmstalker.springkube.enums

import io.lonmstalker.springkube.constants.ErrorConstants
import io.lonmstalker.springkube.exception.AuthException
import org.apache.commons.lang3.StringUtils

enum class Provider(val grantType: String, val login: String, val password: String) {
    PASSWORD("password", "username", "password"),
    CLIENT_CREDENTIALS("client_credentials", "client_id", "client_secret"),
    AUTHORIZATION_CODE("authorization_code", "username", "password"),
    REFRESH_TOKEN("refresh_token", StringUtils.EMPTY, StringUtils.EMPTY);
}

private val providerByGrantType: Map<String, Provider> = Provider.values().associateBy { it.grantType }

fun providerByGrant(value: String): Provider = providerByGrantType[value] ?: throw exceptionUnknownGrantType()

fun exceptionUnknownGrantType() = AuthException(ErrorConstants.OAUTH2_GRANT_TYPE_UNKNOWN, "grant_type not found")