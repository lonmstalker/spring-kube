package io.lonmstalker.springkube.config

import io.lonmstalker.springkube.config.properties.AuthProperties
import io.lonmstalker.springkube.jwt.JwtCustomizer
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import java.util.*

// example : https://github.com/spring-projects/spring-authorization-server/tree/main/samples/featured-authorizationserver
@Configuration(proxyBeanMethods = false)
class SecurityConfig(private val authProperties: AuthProperties) {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationFilterChain(
        http: HttpSecurity,
        jwtEncoder: JwtEncoder,
        settings: AuthorizationServerSettings,
        repository: RegisteredClientRepository,
        userDetailsService: UserDetailsService,
        clientRepository: RegisteredClientRepository,
        authenticationManager: AuthenticationManager,
        authenticationProvider: AuthenticationProvider,
    ): SecurityFilterChain =
        http
            .cors().disable()
            .csrf().disable()
            .httpBasic().disable()
            .formLogin()
            .failureForwardUrl(this.authProperties.failureUrl)
            .loginPage(this.authProperties.loginPage)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(LoginUrlAuthenticationEntryPoint(this.authProperties.loginPage))
            .and()
            .authenticationProvider(authenticationProvider)
            .userDetailsService(userDetailsService)
            .authenticationManager(authenticationManager)
            .apply(OAuth2AuthorizationServerConfigurer())
            .tokenGenerator(JwtGenerator(jwtEncoder).apply { this.setJwtCustomizer(JwtCustomizer()) })
            .authorizationServerSettings(settings)
            .registeredClientRepository(repository)
            .and()
            .build()

    @Bean
    fun registeredClientRepository(props: OAuth2ClientProperties): RegisteredClientRepository =
        props
            .registration
            .entries
            .map { it.value.toRegisteredClient(it.key) }
            .run { InMemoryRegisteredClientRepository(this) }

    private fun OAuth2ClientProperties.Registration.toRegisteredClient(key: String): RegisteredClient =
        RegisteredClient.withId(key)
            .clientId(this.clientId)
            .clientName(this.clientName)
            .clientSecret(this.clientSecret)
            .authorizationGrantTypes {
                it.addAll(this.authorizationGrantType.split(",").map { AuthorizationGrantType(it.trim()) })
            }
            .scopes { this.scope }
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .build()
}