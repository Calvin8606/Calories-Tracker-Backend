package com.mycalories.CaloriesTracker.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserProfileDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String goal;

    @NotBlank
    private String gender;

    @NotNull
    private Integer heightFeet;

    @NotNull
    private Integer heightInches;

    @NotNull
    private Double weightLbs;

    @NotBlank
    private String activityLevel;

    // New fields for BMR calculations
    private Double maintenanceCalories;
    private Double gainCalories;
    private Double lossCalories;
}
