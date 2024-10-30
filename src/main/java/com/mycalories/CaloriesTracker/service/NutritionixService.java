package com.mycalories.CaloriesTracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycalories.CaloriesTracker.dto.FoodItemDto;
import com.mycalories.CaloriesTracker.dto.NutrientDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private ObjectMapper objectMapper;

    private HttpHeaders headers;

    @PostConstruct
    private void initHeaders() {
        headers = new HttpHeaders();
        headers.set("x-app-id", apiId);
        headers.set("x-app-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    // Search for food items
    public List<FoodItemDto> searchFood(String query) {
        String url = apiBaseUrl + "/search/instant?query=" + query;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        List<FoodItemDto> foodItems = new ArrayList<>();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // Handle branded items
            JsonNode brandedNode = rootNode.path("branded");
            for (JsonNode node : brandedNode) {
                FoodItemDto foodItem = new FoodItemDto();
                foodItem.setTagId(null);  // No tagId for branded items
                foodItem.setFoodName(node.path("food_name").asText(null));
                foodItem.setBrandName(node.path("brand_name").asText(null));
                foodItem.setNixItemId(node.path("nix_item_id").asText(null));
                foodItems.add(foodItem);
            }

            // Handle common items
            JsonNode commonNode = rootNode.path("common");
            for (JsonNode node : commonNode) {
                FoodItemDto foodItem = new FoodItemDto();
                foodItem.setTagId(node.path("tag_id").asText(null));
                foodItem.setFoodName(node.path("food_name").asText(null));
                foodItem.setBrandName(null);  // No brand name for common items
                foodItem.setNixItemId(null);  // No nixItemId for common items
                foodItems.add(foodItem);
            }

        } catch (Exception e) {
            System.out.println("Error in searchFood: " + e.getMessage());
        }

        return foodItems;
    }

    // Get nutrients for common items using food name
    public NutrientDto getNutrientsByName(String foodName) {
        String url = apiBaseUrl + "/natural/nutrients";
        NutrientDto nutrient = new NutrientDto();
        try {
            String requestBody = "{\"query\":\"" + foodName + "\"}";
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode foodsArray = rootNode.path("foods");
                System.out.println("Backend raw response: " + response.getBody()); // Log raw response

                if (foodsArray.isArray() && !foodsArray.isEmpty()) {
                    JsonNode foodNode = foodsArray.get(0);
                    nutrient.setFoodName(foodNode.path("food_name").asText(null));
                    nutrient.setCalories(String.valueOf(foodNode.path("nf_calories").asDouble(0.0)));
                    nutrient.setProtein(String.valueOf(foodNode.path("nf_protein").asDouble(0.0)));
                    nutrient.setServingWeightGrams(String.valueOf(foodNode.path("serving_weight_grams").asDouble(0.0)));

                    System.out.println("Parsed Nutrient Data: " + nutrient); // Log parsed data
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getNutrientsByName: " + e.getMessage());
        }
        return nutrient;
    }

    // Get nutrients for branded items using nixItemId
    public NutrientDto getNutrientsByNixItemId(String nixItemId) {
        String url = apiBaseUrl + "/search/item?nix_item_id=" + nixItemId;
        NutrientDto nutrient = new NutrientDto();

        try {
            String requestBody = "{\"nix_item_id\":\"" + nixItemId + "\"}";
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                // Check the JSON structure and parse accordingly
                JsonNode foodsArray = rootNode.path("foods");
                if (foodsArray.isArray() && !foodsArray.isEmpty()) {
                    JsonNode foodNode = foodsArray.get(0);
                    nutrient.setFoodName(foodNode.path("food_name").asText(null));
                    nutrient.setBrandName(foodNode.path("brand_name").asText(null));
                    nutrient.setCalories(String.valueOf(foodNode.path("nf_calories").asDouble(0.0)));
                    nutrient.setProtein(String.valueOf(foodNode.path("nf_protein").asDouble(0.0)));
                    nutrient.setServingWeightGrams(String.valueOf(foodNode.path("serving_weight_grams").asDouble(0.0)));
                } else {
                    System.out.println("Branded JSON parsing issue: " + response.getBody());
                }
            } else {
                System.out.println("Unexpected status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Error in getNutrientsByNixItemId: " + e.getMessage());
        }

        return nutrient;
    }

}
