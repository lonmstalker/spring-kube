package io.lonmstalker.springkube.config

import io.lonmstalker.springkube.authentication.UserTokenResponseClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import java.util.*

// example : https://github.com/spring-projects/spring-authorization-server/tree/main/samples/featured-authorizationserver
@Configuration(proxyBeanMethods = false)
class SecurityConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationFilterChain(
        http: HttpSecurity,
        userDetailsService: UserDetailsService,
        authenticationManager: AuthenticationManager,
        authenticationProvider: AuthenticationProvider,
        userTokenResponseClient: UserTokenResponseClient
    ): SecurityFilterChain =
        http
            .cors().disable()
            .csrf().disable()
            .httpBasic()
            .and()
            .formLogin().disable()
            .oauth2Login().tokenEndpoint().accessTokenResponseClient(userTokenResponseClient)
            .and().and()
            .exceptionHandling()
            .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and()
            .authenticationProvider(authenticationProvider)
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests()
            .requestMatchers("/api/v1/public/**", "/oauth2/**").permitAll()
            .requestMatchers("/api/v1/oauth2/**").authenticated()
            .requestMatchers("/api/v1/current-user/**").fullyAuthenticated()
            .anyRequest().authenticated()
            .and()
            .authenticationManager(authenticationManager)
            .build()
}