package io.lonmstalker.springkube.utils

import io.lonmstalker.springkube.constants.ErrorConstants
import io.lonmstalker.springkube.enums.Provider
import io.lonmstalker.springkube.enums.providerByGrantType
import io.lonmstalker.springkube.exception.AuthException

object ExceptionUtils {

    fun providerByGrant(value: String): Provider = providerByGrantType[value] ?: throw exceptionUnknownGrantType()
    fun exceptionUnknownGrantType() = AuthException(ErrorConstants.OAUTH2_GRANT_TYPE_UNKNOWN, "grant_type not found")
}