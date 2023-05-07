package io.lonmstalker.springkube.jwt

import io.lonmstalker.springkube.constants.JwtConstants.EMAIL
import io.lonmstalker.springkube.constants.JwtConstants.FIRST_NAME
import io.lonmstalker.springkube.constants.JwtConstants.LOGIN_TIME
import io.lonmstalker.springkube.constants.JwtConstants.ROLE
import io.lonmstalker.springkube.constants.JwtConstants.USER_GROUP_ID
import io.lonmstalker.springkube.constants.JwtConstants.USER_ID
import io.lonmstalker.springkube.constants.JwtConstants.USER_NAME
import io.lonmstalker.springkube.model.CustomTokenAuthentication
import io.lonmstalker.springkube.model.UserInfo
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.util.*

class JwtCustomConverter : Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    override fun convert(source: Jwt): Mono<AbstractAuthenticationToken> =
        Mono.fromRunnable { CustomTokenAuthentication(source.toUserInfo()) }

    private fun Jwt.toUserInfo() =
        UserInfo(
            role = this.getClaimAsString(ROLE),
            username = this.getClaimAsString(USER_NAME),
            userId = UUID.fromString(this.getClaimAsString(USER_ID)),
            userGroupId = UUID.fromString(this.getClaimAsString(USER_GROUP_ID)),
            loginTime = OffsetDateTime.parse(this.getClaimAsString(LOGIN_TIME)),
            firstName = this.getClaimAsString(FIRST_NAME),
            email = this.getClaimAsString(EMAIL)
        )
}