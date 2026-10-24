package com.ralphmarondev.portfolio.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtFilter: JwtFilter): SecurityFilterChain {
        http.csrf { it.disable() }
            .cors { }
            .authorizeHttpRequests { auth ->
                // Public
                auth.requestMatchers("/account/login/").permitAll()
                auth.requestMatchers(HttpMethod.GET, "/project/**", "/skill/**").permitAll()
                auth.requestMatchers(HttpMethod.POST, "/message/").permitAll()

                // Any logged-in user
                auth.requestMatchers("/account/me/").authenticated()

                // Everything else (project/skill writes, reading messages) is admin only
                auth.anyRequest().hasRole(RoleConstants.ADMIN)
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
