package io.lonmstalker.springkube.config

import io.lonmstalker.springkube.config.properties.AppProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import java.util.*

// example : https://github.com/spring-projects/spring-authorization-server/tree/main/samples/featured-authorizationserver
@Configuration(proxyBeanMethods = false)
class SecurityConfig(private val appProperties: AppProperties) {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationFilterChain(
        http: HttpSecurity,
        userDetailsService: UserDetailsService,
        authenticationManager: AuthenticationManager,
        authenticationProvider: AuthenticationProvider,
    ): SecurityFilterChain =
        http
            .cors().disable()
            .csrf().disable()
            .httpBasic().disable()
            .formLogin()
            .failureForwardUrl(this.appProperties.auth.failureUrl)
            .loginPage(this.appProperties.auth.loginPage)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(LoginUrlAuthenticationEntryPoint(this.appProperties.auth.loginPage))
            .and()
            .authenticationProvider(authenticationProvider)
            .userDetailsService(userDetailsService)
            .authenticationManager(authenticationManager)
            .authorizeHttpRequests()
            .anyRequest().authenticated()
            .requestMatchers("**/public/**").permitAll()
            .and()
            .build()
}