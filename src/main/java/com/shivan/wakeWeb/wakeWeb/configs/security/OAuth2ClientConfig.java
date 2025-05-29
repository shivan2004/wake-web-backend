package com.shivan.wakeWeb.wakeWeb.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OAuth2ClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }


    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(
            ClientRegistrationRepository clients) {
        return new InMemoryOAuth2AuthorizedClientService(clients);
    }
}