package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.config.properties.AppProperties
import io.lonmstalker.springkube.constants.ErrorConstants.OAUTH2_TOKEN_NOT_FOUND
import io.lonmstalker.springkube.constants.JwtConstants.EMAIL
import io.lonmstalker.springkube.constants.JwtConstants.FIRST_NAME
import io.lonmstalker.springkube.constants.JwtConstants.LOGIN_TIME
import io.lonmstalker.springkube.constants.JwtConstants.ROLE
import io.lonmstalker.springkube.constants.JwtConstants.USER_GROUP_ID
import io.lonmstalker.springkube.constants.JwtConstants.USER_ID
import io.lonmstalker.springkube.constants.JwtConstants.USER_NAME
import io.lonmstalker.springkube.enums.TokenType
import io.lonmstalker.springkube.exception.AuthException
import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.model.User
import io.lonmstalker.springkube.model.UserToken
import io.lonmstalker.springkube.model.UserTokenInfo
import io.lonmstalker.springkube.model.system.CreateTokenSettings
import io.lonmstalker.springkube.repository.TokenRepository
import io.lonmstalker.springkube.service.TokenService
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

@Service
class TokenServiceImpl(
    private val jwtEncoder: JwtEncoder,
    private val jwtDecoder: JwtDecoder,
    private val clockHelper: ClockHelper,
    private val tokenRepository: TokenRepository, appProperties: AppProperties,
) : TokenService {
    private val issuer = appProperties.auth.issuer
    private val tokenProperties = appProperties.token

    @Transactional
    override fun cleanExpiredTokens() {
        this.tokenRepository.cleanTokenExpiredAtBefore(this.clockHelper.clockInstant().plusSeconds(30))
    }

    override fun parseToken(token: String): UUID =
        this.jwtDecoder
            .decode(token)
            .getClaim<String>(USER_ID)
            .run { UUID.fromString(this) }

    override fun findByValueAndType(value: String, type: TokenType): UserToken =
        this.tokenRepository.findByValueAndType(value, type.name)
            ?: throw AuthException(OAUTH2_TOKEN_NOT_FOUND, "token $value not found")

    @Transactional
    override fun createToken(user: User, client: String, settings: CreateTokenSettings): UserTokenInfo {
        val issuedAt = clockHelper.clockInstant()
        val accessToken = this.createToken(issuedAt, user, TokenType.ACCESS, client)

        val refreshToken = if (settings.createRefreshToken) {
            this.createToken(issuedAt, user, TokenType.REFRESH, client)
        } else {
            null
        }

        this.update(accessToken, refreshToken)
        return UserTokenInfo(
            BEARER.value,
            issuedAt.plusSeconds(TokenType.ACCESS.toTtl()),
            setOf(user.role.name),
            accessToken.value,
            refreshToken?.value
        )
    }

    private fun update(accessToken: UserToken, refreshToken: UserToken?) {
        this.tokenRepository.deleteByClientAndUserId(accessToken.userId, accessToken.client)
        if (refreshToken != null) {
            this.tokenRepository.saveAll(listOf(accessToken, refreshToken))
        } else {
            this.tokenRepository.save(accessToken)
        }
    }

    private fun createToken(issuedAt: Instant, user: User, type: TokenType, client: String): UserToken =
        JwtClaimsSet.builder()
            .issuer(this.issuer)
            .issuedAt(issuedAt)
            .expiresAt(issuedAt.plusSeconds(type.toTtl()))
            .claims { user.fillClaims(it, type) }
            .subject(user.username)
            .build()
            .run { jwtEncoder.encode(JwtEncoderParameters.from(this)) }
            .tokenValue
            .run {
                UserToken(
                    userId = user.id,
                    value = this,
                    client = client,
                    issuedAt = issuedAt,
                    type = type
                )
            }

    private fun User.fillClaims(claims: MutableMap<String, Any>, type: TokenType) {
        claims[USER_ID] = this.id
        claims[USER_NAME] = this.username
        if (type == TokenType.ACCESS) {
            claims[ROLE] = this.role
            claims[EMAIL] = this.email
            claims[FIRST_NAME] = this.firstName
            claims[LOGIN_TIME] = this.lastLogin!!.toEpochSecond(ZoneOffset.UTC)
            claims[USER_GROUP_ID] = this.userGroupId!!
        }
    }

    private fun TokenType.toTtl() =
        if (this == TokenType.ACCESS) {
            tokenProperties.accessTtl
        } else {
            tokenProperties.refreshTtl
        }
}