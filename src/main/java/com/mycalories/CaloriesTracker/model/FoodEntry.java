package com.mycalories.CaloriesTracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "food_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;  // Food item name
    private int calories;
    private double protein;
    private double servingWeightGrams;

    @ManyToOne
    @JoinColumn(name = "daily_calories_id")
    @JsonIgnore
    private UserDailyCalories dailyCaloriesRecord;
}
