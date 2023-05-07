package io.lonmstalker.springkube.config.security

import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import java.security.interfaces.RSAPublicKey
import java.util.*

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class ServletSecurityConfig {

    @Bean
    @ConditionalOnMissingBean
    fun jwtDecoder(
        @Value("\${app.security.public-key-path}") pubKeyPath: String,
        resourceLoader: ResourceLoader
    ): JwtDecoder =
        resourceLoader
            .getResource(pubKeyPath)
            .run { CertificateFactory().engineGenerateCertificate(this.inputStream).publicKey }
            .run { NimbusJwtDecoder.withPublicKey(this as RSAPublicKey).build() }

}