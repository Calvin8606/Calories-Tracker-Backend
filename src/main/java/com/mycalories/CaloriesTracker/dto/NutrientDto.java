package com.mycalories.CaloriesTracker.dto;

import lombok.Data;

@Data
public class NutrientDto {
    private String brandName;
    private String foodName;
    private String calories;
    private String protein;
    private String servingWeightGrams;

    // Getters and setters
}
