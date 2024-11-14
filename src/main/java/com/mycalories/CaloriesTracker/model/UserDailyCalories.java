package com.mycalories.CaloriesTracker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user_daily_calories")
@Data
public class UserDailyCalories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // Reference to the user

    private LocalDate date; // The date

    private int totalCalories; // Total calories consumed that day

    @OneToMany(mappedBy = "dailyCaloriesRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodEntry> foodEntries;

}