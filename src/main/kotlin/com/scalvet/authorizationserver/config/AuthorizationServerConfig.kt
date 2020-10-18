package com.scalvet.authorizationserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.keys.KeyManager
import org.springframework.security.crypto.keys.StaticKeyGeneratingKeyManager
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import java.util.*

@EnableWebSecurity
@Import(OAuth2AuthorizationServerConfiguration::class)
class AuthorizationServerConfig {
    // @formatter:off
    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        val registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("messaging-client")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://localhost:8080/authorized")
                .scope("message.read")
                .scope("message.write")
                .clientSettings { clientSettings: ClientSettings -> clientSettings.requireUserConsent(true) }
                .build()
        return InMemoryRegisteredClientRepository(registeredClient)
    }

    // @formatter:on
    @Bean
    fun keyManager(): KeyManager {
        return StaticKeyGeneratingKeyManager()
    }

    // @formatter:off
    @Bean
    fun users(): UserDetailsService {
        val user = User.withDefaultPasswordEncoder()
                .username("user1")
                .password("password")
                .roles("USER")
                .build()
        return InMemoryUserDetailsManager(user)
    } // @formatter:on
}