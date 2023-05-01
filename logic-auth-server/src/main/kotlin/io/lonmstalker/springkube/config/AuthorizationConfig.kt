package io.lonmstalker.springkube.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import io.lonmstalker.springkube.config.properties.AuthProperties
import io.lonmstalker.springkube.config.properties.JwtProperties
import org.apache.commons.lang3.StringUtils
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.stereotype.Component
import org.springframework.util.ResourceUtils
import java.security.KeyStore
import java.util.*

@Component
class AuthorizationConfig(

) {

    @Bean
    fun authenticationProvider(passwordEncoder: PasswordEncoder, userDetailsService: UserDetailsService) =
        DaoAuthenticationProvider(passwordEncoder)
            .apply { this.setUserDetailsService(userDetailsService) }

    @Bean
    fun authorizationServerSettings(authProperties: AuthProperties): AuthorizationServerSettings =
        AuthorizationServerSettings
            .builder()
            .issuer(authProperties.issuer)
            .build()

    @Bean
    fun jwtEncoder(jwtProperties: JwtProperties): NimbusJwtEncoder {
        val keyPass = jwtProperties.keyPass.toCharArray()
        val storePass = jwtProperties.keyStorePass.toCharArray()
        try {
            val keyStore = KeyStore.getInstance(ResourceUtils.getFile(jwtProperties.location), storePass)
            val rsaKey = RSAKey.load(keyStore, jwtProperties.alias, keyPass)
            return NimbusJwtEncoder(ImmutableJWKSet(JWKSet(rsaKey)))
        } finally {
            this.clearJwt(jwtProperties, keyPass, storePass)
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    private fun clearJwt(jwtProperties: JwtProperties, keyPass: CharArray, storePass: CharArray) {
        jwtProperties.alias = StringUtils.EMPTY
        jwtProperties.alias = StringUtils.EMPTY
        jwtProperties.keyPass = StringUtils.EMPTY
        jwtProperties.location = StringUtils.EMPTY
        jwtProperties.keyStorePass = StringUtils.EMPTY
        Arrays.fill(keyPass, '0')
        Arrays.fill(storePass, '0')
    }

}