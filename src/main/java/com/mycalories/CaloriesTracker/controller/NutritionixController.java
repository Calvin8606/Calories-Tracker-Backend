package com.mycalories.CaloriesTracker.controller;

import com.mycalories.CaloriesTracker.dto.FoodItemDto;
import com.mycalories.CaloriesTracker.dto.NutrientDto;
import com.mycalories.CaloriesTracker.service.NutritionixService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;

@RestController
@RequestMapping("/api/nutrition")
public class NutritionixController {

    @Autowired
    private NutritionixService nutritionixService;

    // Search for food items
    @GetMapping("/search")
    public List<FoodItemDto> searchFood(@RequestParam String query) {
        return nutritionixService.searchFood(query);
    }

    // Get nutrients for a common item by foodName
    @PostMapping("/food/common/{foodName}/nutrients")
    public NutrientDto getNutrientsForCommon(@PathVariable String foodName) {
        return nutritionixService.getNutrientsByName(foodName);
    }



    // Get nutrients for a branded item by nixItemId
    @GetMapping("/food/branded/{nixItemId}/nutrients")
    public NutrientDto getNutrientsForBranded(@PathVariable String nixItemId) {
        return nutritionixService.getNutrientsByNixItemId(nixItemId);
    }

}

