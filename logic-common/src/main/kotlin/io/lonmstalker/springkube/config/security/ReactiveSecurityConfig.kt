package io.lonmstalker.springkube.config.security

import io.lonmstalker.springkube.jwt.JwtCustomConverter
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain
import java.security.interfaces.RSAPublicKey

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
class ReactiveSecurityConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationFilterChain(
        http: ServerHttpSecurity,
        jwtDecoder: ReactiveJwtDecoder,
    ): SecurityWebFilterChain =
        http
            .csrf().disable()
            .cors().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable()
            .requestCache().disable()
            .anonymous().disable()
            .authorizeExchange()
            .anyExchange().authenticated()
            .pathMatchers("**/public/**").permitAll()
            .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(JwtCustomConverter())
            .jwtDecoder(jwtDecoder)
            .and().and()
            .build()

    @Bean
    fun nimbusJwtDecoder(key: RSAPublicKey): ReactiveJwtDecoder =
        NimbusReactiveJwtDecoder
            .withPublicKey(key)
            .build()
}