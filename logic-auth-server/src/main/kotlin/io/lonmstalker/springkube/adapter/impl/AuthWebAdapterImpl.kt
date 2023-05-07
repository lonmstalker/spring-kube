package io.lonmstalker.springkube.adapter.impl

import io.lonmstalker.springkube.adapter.AuthWebAdapter
import io.lonmstalker.springkube.constants.ErrorConstants
import io.lonmstalker.springkube.dto.TokenResponseDto
import io.lonmstalker.springkube.enums.Provider
import io.lonmstalker.springkube.enums.exceptionUnknownGrantType
import io.lonmstalker.springkube.enums.providerByGrant
import io.lonmstalker.springkube.exception.AuthException
import io.lonmstalker.springkube.mapper.TokenMapper
import io.lonmstalker.springkube.provider.TokenProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.stereotype.Component

@Component
class AuthWebAdapterImpl(
    private val tokenMapper: TokenMapper,
    private val tokenProviders: List<TokenProvider>
) : AuthWebAdapter {

    override fun login(requestBody: Map<String, String>) {
        TODO("Not yet implemented")
    }

    override fun authorize(requestBody: Map<String, String>) {
        TODO("Not yet implemented")
    }

    override fun authenticate(requestBody: Map<String, String>): TokenResponseDto {
        val client = SecurityContextHolder.getContext().authentication.principal as String
        val grantType = this.getGrantType(requestBody)
        return this.tokenProviders
            .asSequence()
            .filter { it.supportGrantType(grantType) }
            .map { it.authenticate(requestBody, client) }
            .firstOrNull()
            ?.let { this.tokenMapper.toDto(it) }
            ?: throw AuthException(ErrorConstants.OAUTH2_GRANT_TYPE_UNKNOWN, "invalid request")
    }

    private fun getGrantType(requestBody: Map<String, String>): Provider =
        when (providerByGrant(this.getGrantTypeFromRequest(requestBody))) {
            Provider.AUTHORIZATION_CODE -> Provider.AUTHORIZATION_CODE
            Provider.CLIENT_CREDENTIALS -> Provider.CLIENT_CREDENTIALS
            Provider.REFRESH_TOKEN -> Provider.REFRESH_TOKEN
            else -> throw exceptionUnknownGrantType()
        }

    private fun getGrantTypeFromRequest(requestBody: Map<String, String>) =
        requestBody[OAuth2ParameterNames.GRANT_TYPE] ?: throw exceptionUnknownGrantType()
}