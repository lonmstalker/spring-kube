package io.lonmstalker.springkube.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.util.ResourceUtils
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class ServletSecurityConfig {

//    @Bean
    fun jwtDecoder(@Value("\${app.security.public-key-path}") pubKeyPath: String): JwtDecoder =
        ResourceUtils
            .getFile(pubKeyPath)
            .readBytes()
            .run { KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(this)) }
            .run { NimbusJwtDecoder.withPublicKey(this as RSAPublicKey).build() }
}