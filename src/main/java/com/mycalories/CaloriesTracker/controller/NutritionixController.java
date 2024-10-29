package com.mycalories.CaloriesTracker.controller;

import com.mycalories.CaloriesTracker.service.NutritionixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nutritionix")
public class NutritionixController {

    @Autowired
    private NutritionixService nutritionixService;

    @GetMapping("/search")
    public ResponseEntity<String> searchFood(@RequestParam String query) {
        try {
            String result = nutritionixService.searchFood(query);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
