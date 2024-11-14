package com.mycalories.CaloriesTracker.service;

import com.mycalories.CaloriesTracker.model.FoodEntry;
import com.mycalories.CaloriesTracker.model.User;
import com.mycalories.CaloriesTracker.model.UserDailyCalories;
import com.mycalories.CaloriesTracker.repository.FoodEntryRepository;
import com.mycalories.CaloriesTracker.repository.UserDailyCaloriesRepository;
import com.mycalories.CaloriesTracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserDailyCaloriesService {

    @Autowired
    UserDailyCaloriesRepository userDailyCaloriesRepository;

    @Autowired
    FoodEntryRepository foodEntryRepository;

    @Autowired
    UserRepository userRepository;

    public Optional<UserDailyCalories> getDailyCalories(LocalDate date) {
        // Retrieve the daily calories record for the specified date only
        return userDailyCaloriesRepository.findByDate(date);
    }

    public List<UserDailyCalories> getCaloriesForDateRange(LocalDate startDate, LocalDate endDate) {
        return userDailyCaloriesRepository.dateBetween(startDate, endDate);
    }

    // This method will add a food entry for a specific date
    public boolean addFoodEntry(LocalDate date, FoodEntry newFoodEntry, Principal principal) {
        // Use principal to get the authenticated user's email
        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());

        // Check if the user exists
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User currentUser = optionalUser.get();

        // Retrieve or create UserDailyCalories record for the specific date
        UserDailyCalories userDailyCalories = userDailyCaloriesRepository
                .findByDate(date)
                .orElseGet(() -> {
                    UserDailyCalories newRecord = new UserDailyCalories();
                    newRecord.setUser(currentUser); // Associate with current user
                    newRecord.setDate(date);
                    newRecord.setTotalCalories(0);
                    return userDailyCaloriesRepository.save(newRecord);
                });

        // Link the new food entry to the UserDailyCalories record
        newFoodEntry.setDailyCaloriesRecord(userDailyCalories);
        foodEntryRepository.save(newFoodEntry);

        // Update total calories for the day
        int updatedTotalCalories = userDailyCalories.getTotalCalories() + newFoodEntry.getCalories();
        userDailyCalories.setTotalCalories(updatedTotalCalories);
        userDailyCaloriesRepository.save(userDailyCalories);

        return true;
    }

    @Transactional
    public boolean removeFoodEntry(Long foodEntryId) {
        Optional<FoodEntry> foodEntryOpt = foodEntryRepository.findById(foodEntryId);

        if (foodEntryOpt.isPresent()) {
            FoodEntry foodEntry = foodEntryOpt.get();
            UserDailyCalories dailyCaloriesRecord = foodEntry.getDailyCaloriesRecord();

            if (dailyCaloriesRecord != null) {
                // Update total calories
                int updatedTotalCalories = Math.max(0, dailyCaloriesRecord.getTotalCalories() - foodEntry.getCalories());
                dailyCaloriesRecord.setTotalCalories(updatedTotalCalories);
                userDailyCaloriesRepository.save(dailyCaloriesRecord);
            }

            // Remove the food entry
            foodEntryRepository.delete(foodEntry);

            return true;
        } else {
            return false;
        }
    }

}
