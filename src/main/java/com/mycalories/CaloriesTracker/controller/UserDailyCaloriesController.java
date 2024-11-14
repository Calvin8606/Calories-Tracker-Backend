package com.mycalories.CaloriesTracker.controller;

import com.mycalories.CaloriesTracker.model.FoodEntry;
import com.mycalories.CaloriesTracker.model.UserDailyCalories;
import com.mycalories.CaloriesTracker.service.UserDailyCaloriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/calories")
public class UserDailyCaloriesController {

    @Autowired
    private UserDailyCaloriesService userDailyCaloriesService;

    // Get calorie data for a specific day
    @GetMapping("/date/{date}")
    public ResponseEntity<UserDailyCalories> getDailyCalories(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        Optional<UserDailyCalories> dailyCaloriesOpt = userDailyCaloriesService.getDailyCalories(localDate);

        return dailyCaloriesOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/date/{date}/addFood")
    public ResponseEntity<?> addFoodEntry(
            @PathVariable String date,
            @RequestBody FoodEntry newFoodEntry,
            Principal principal) { // Add Principal as a parameter
        LocalDate localDate = LocalDate.parse(date);

        // Pass Principal to addFoodEntry method
        boolean isUpdated = userDailyCaloriesService.addFoodEntry(localDate, newFoodEntry, principal);

        if (isUpdated) {
            return ResponseEntity.ok("Food entry added successfully and total calories updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No record found for the specified date");
        }
    }

    @DeleteMapping("/food/{foodEntryId}")
    public ResponseEntity<?> removeFoodEntry(@PathVariable Long foodEntryId) {
        boolean isRemoved = userDailyCaloriesService.removeFoodEntry(foodEntryId);

        if (isRemoved) {
            return ResponseEntity.ok("Food entry removed successfully and total calories updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food entry not found");
        }
    }
}