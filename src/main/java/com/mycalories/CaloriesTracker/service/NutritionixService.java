package com.mycalories.CaloriesTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class NutritionixService {

    @Value("${nutritionix.api-id}")
    private String apiId;

    @Value("${nutritionix.api-key}")
    private String apiKey;

    @Value("${nutritionix.base-url}")
    private String apiBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public String searchFood(String query) {
        String url = apiBaseUrl + "/search/instant?query=" + query;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-app-id", apiId);
        headers.set("x-app-key", apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        } catch (HttpClientErrorException e) {
            return "Client error: " + e.getStatusCode() + " / " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "Error during API call: " + e.getMessage();
        }
    }
}
