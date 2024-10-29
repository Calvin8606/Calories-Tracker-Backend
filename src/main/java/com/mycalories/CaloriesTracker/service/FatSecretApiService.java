package com.mycalories.CaloriesTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FatSecretApiService {

    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final WebClient webClient;

    @Autowired
    public FatSecretApiService(OAuth2AuthorizedClientManager authorizedClientManager, WebClient webClient) {
        this.authorizedClientManager = authorizedClientManager;
        this.webClient = webClient;
    }

    // Method to get the access token
    public String getAccessToken() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("fatsecret")
                .principal("FatSecretClient")
                .build();

        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
            return authorizedClient.getAccessToken().getTokenValue();
        } else {
            throw new RuntimeException("Unable to authorize the FatSecret client.");
        }
    }

    // Method to search for food items in FatSecret API
    public Mono<String> searchFood(String searchExpression) {
        String accessToken = getAccessToken();
        String url = "https://platform.fatsecret.com/rest/server.api?method=food.search&search_expression="
                + searchExpression + "&format=json";

        return webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> {
                    throw new RuntimeException("Error during FatSecret API request: " + e.getMessage());
                });
    }
}