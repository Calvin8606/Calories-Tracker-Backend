package com.mycalories.CaloriesTracker.repository;

import com.mycalories.CaloriesTracker.model.FoodEntry;
import com.mycalories.CaloriesTracker.model.UserDailyCalories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
    List<FoodEntry> findByDailyCaloriesRecord(UserDailyCalories dailyCaloriesRecord);
}
