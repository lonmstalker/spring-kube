package io.lonmstalker.springkube.utils

import io.lonmstalker.springkube.constants.ErrorConstants
import io.lonmstalker.springkube.enums.Provider
import io.lonmstalker.springkube.exception.AuthException
import org.apache.commons.lang3.StringUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated

object OAuth2Utils {

    fun Map<String, String>.toUsernamePasswordToken(): UsernamePasswordAuthenticationToken {
        val login = this[Provider.CLIENT_CREDENTIALS.principal]
            ?: throw exceptionOauth2BadRequest("login not found")
        val password = this[Provider.CLIENT_CREDENTIALS.credentials]
            ?: throw exceptionOauth2BadRequest("password not found")
        return unauthenticated(login, password)
    }

    fun exceptionOauth2BadRequest(message: String = StringUtils.EMPTY) =
        AuthException(ErrorConstants.OAUTH2_BAD_REQUEST, message)
}