package io.lonmstalker.springkube.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import io.lonmstalker.springkube.checker.PostAuthenticationChecker
import io.lonmstalker.springkube.checker.PreAuthenticationChecker
import io.lonmstalker.springkube.config.properties.AppProperties
import io.lonmstalker.springkube.constants.JwtConstants.BOUNCY_CASTLE_PROVIDER_NAME
import io.lonmstalker.springkube.constants.JwtConstants.STORE_TYPE
import org.apache.commons.lang3.StringUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.util.ResourceUtils.getFile
import java.io.FileInputStream
import java.security.KeyStore
import java.util.*

@Configuration
class AuthorizationConfig {

    @Bean
    fun authenticationProvider(
        passwordEncoder: PasswordEncoder,
        userDetailsService: UserDetailsService,
        preAuthenticationChecker: PreAuthenticationChecker,
        postAuthenticationChecker: PostAuthenticationChecker
    ) =
        DaoAuthenticationProvider(passwordEncoder)
            .apply { this.setUserDetailsService(userDetailsService) }
            .apply { this.setPostAuthenticationChecks(postAuthenticationChecker) }
            .apply { this.setPreAuthenticationChecks(preAuthenticationChecker) }

    @Bean
    fun jwtEncoder(appProperties: AppProperties): NimbusJwtEncoder {
        val jwtProperties = appProperties.jwt
        val keyPass = jwtProperties.keyPass.toCharArray()
        val storePass = jwtProperties.keyStorePass.toCharArray()
        return try {
            KeyStore.getInstance(STORE_TYPE, BOUNCY_CASTLE_PROVIDER_NAME)
                .apply { this.load(FileInputStream(getFile(jwtProperties.location)), storePass) }
                .run { RSAKey.load(this, jwtProperties.alias, keyPass) }
                .run { NimbusJwtEncoder(ImmutableJWKSet(JWKSet(this))) }
        } finally {
            this.clearJwt(jwtProperties, keyPass, storePass)
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    private fun clearJwt(jwtProperties: AppProperties.JwtProperties, keyPass: CharArray, storePass: CharArray) {
        jwtProperties.alias = StringUtils.EMPTY
        jwtProperties.alias = StringUtils.EMPTY
        jwtProperties.keyPass = StringUtils.EMPTY
        jwtProperties.location = StringUtils.EMPTY
        jwtProperties.keyStorePass = StringUtils.EMPTY
        Arrays.fill(keyPass, '0')
        Arrays.fill(storePass, '0')
    }

}