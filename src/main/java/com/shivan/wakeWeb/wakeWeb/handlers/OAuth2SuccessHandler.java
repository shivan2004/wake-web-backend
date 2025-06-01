package com.shivan.wakeWeb.wakeWeb.handlers;

import com.shivan.wakeWeb.wakeWeb.entities.User;
import com.shivan.wakeWeb.wakeWeb.security.JWTService;
import com.shivan.wakeWeb.wakeWeb.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JWTService jwtService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Value("${frontend.url}")
    private String frontendURL;

    private final WebClient webClient = WebClient.create();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        // For GitHub OAuth: fetch email explicitly if missing
        if (email == null && "github".equalsIgnoreCase(token.getAuthorizedClientRegistrationId())) {
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    token.getAuthorizedClientRegistrationId(),
                    token.getName()
            );

            if (client != null && client.getAccessToken() != null) {
                List<Map<String, Object>> emails = webClient.get()
                        .uri("https://api.github.com/user/emails")
                        .headers(h -> h.setBearerAuth(client.getAccessToken().getTokenValue()))
                        .retrieve()
                        .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                        .collectList()
                        .block();

                if (emails != null) {
                    for (Map<String, Object> e : emails) {
                        if (Boolean.TRUE.equals(e.get("primary"))) {
                            email = (String) e.get("email");
                            break;
                        }
                    }
                }
            }
        }

        if (email == null) {
            throw new RuntimeException("OAuth login failed: email could not be retrieved.");
        }

        // Check if user exists, otherwise create one
        User user = userService.getUserByEmail(email);
        if (user == null) {
            user = User.builder()
                    .email(email)
                    .userName(UUID.randomUUID().toString())
                    .build();

            user = userService.save(user);
        }

        // Create access token
        String accessToken = jwtService.generateAccessToken(user);


        // Redirect URL
        String redirectUrl = frontendURL.split(",")[0] +
                "?token=" + accessToken;

        response.sendRedirect(redirectUrl);
    }
}