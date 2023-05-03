package io.lonmstalker.springkube.enums

import org.apache.commons.lang3.StringUtils

enum class Provider(val login: String, val password: String) {
    PASSWORD("username", "password"),
    CLIENT_CREDENTIALS("client_id", "client_secret"),
    AUTHORIZATION_CODE("username", "password"),
    REFRESH_CODE(StringUtils.EMPTY, StringUtils.EMPTY)
}