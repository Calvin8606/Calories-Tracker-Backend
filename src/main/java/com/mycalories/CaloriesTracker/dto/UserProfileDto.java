package com.mycalories.CaloriesTracker.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserProfileDto {
    @NotBlank
    private String goal;

    @NotBlank
    private String gender;

    @NotNull
    private int heightFeet;

    @NotNull
    private int heightInches;

    @NotNull
    private double weightLbs;

    @NotBlank
    private String activityLevel;

    // New fields for BMR calculations
    private double maintenanceCalories;
    private double gainCalories;
    private double lossCalories;
}
