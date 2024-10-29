package com.mycalories.CaloriesTracker.controller;

import com.mycalories.CaloriesTracker.service.FatSecretApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/fatsecret")
public class FatSecretApiController {

    @Autowired
    private FatSecretApiService fatSecretApiService;

    // Endpoint to search for food items
    @GetMapping("/food/search")
    public Mono<ResponseEntity<String>> searchFood(@RequestParam("query") String searchExpression) {
        return fatSecretApiService.searchFood(searchExpression)
                .map(response -> ResponseEntity.ok().body(response))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(500).body("Error: " + e.getMessage())));
    }

    @GetMapping("/token")
    public ResponseEntity<String> getToken() {
        try {
            String accessToken = fatSecretApiService.getAccessToken();
            return ResponseEntity.ok(accessToken);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}