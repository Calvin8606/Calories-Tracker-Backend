package com.mycalories.CaloriesTracker.service;

import com.mycalories.CaloriesTracker.dto.UserProfileDto;
import com.mycalories.CaloriesTracker.model.User;
import com.mycalories.CaloriesTracker.model.UserProfile;
import com.mycalories.CaloriesTracker.repository.UserProfileRepository;
import com.mycalories.CaloriesTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    public UserProfile createOrUpdateUserProfile(UserProfileDto profileDto, Principal principal) {
        // Initialize User
        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }


        // Set up or update the user profile
        User authUser = optionalUser.get();
        UserProfile userProfile;
        if (authUser.getUserProfile() != null) {
            // Update existing UserProfile
            userProfile = authUser.getUserProfile();
        } else {
            // Create new UserProfile
            userProfile = new UserProfile();
            userProfile.setUser(authUser); // Set user reference
        }

        double bmr = calculateBMR(userProfile);
        userProfile.setMaintenanceCalories(calculateMaintenanceCalories(bmr, userProfile.getActivityLevel()));
        userProfile.setGainCalories(userProfile.getMaintenanceCalories() + 500);
        userProfile.setLossCalories(userProfile.getMaintenanceCalories() - 500);

        userProfile.setGoal(profileDto.getGoal());
        userProfile.setGender(profileDto.getGender());
        userProfile.setHeightFeet(profileDto.getHeightFeet());
        userProfile.setHeightInches(profileDto.getHeightInches());
        userProfile.setWeightLbs(profileDto.getWeightLbs());
        userProfile.setActivityLevel(profileDto.getActivityLevel());

        // Save user profile
        return userProfileRepository.save(userProfile);
    }

    public UserProfile getUserProfile(Principal principal) {
        Optional<User> userExist = userRepository.findByEmail(principal.getName());
        if (userExist.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return userExist.get().getUserProfile();
    }

    private double calculateBMR(UserProfile userProfile) {
        double weightKg = userProfile.getWeightLbs() * 0.453592;
        int heightCm = (int) (userProfile.getHeightFeet() * 30.48 + userProfile.getHeightInches() * 2.54);

        if ("male".equalsIgnoreCase(userProfile.getGender())) {
            return 88.362 + (13.397 * weightKg) + (4.799 * heightCm);
        } else if ("female".equalsIgnoreCase(userProfile.getGender())) {
            return 447.593 + (9.247 * weightKg) + (3.098 * heightCm);
        } else {
            throw new IllegalArgumentException("Invalid gender");
        }
    }

    private double calculateMaintenanceCalories(double bmr, String activityLevel) {
        double multiplier = getActivityMultiplier(activityLevel);
        return bmr * multiplier;
    }

    private double getActivityMultiplier(String activityLevel) {
        return switch (activityLevel.toLowerCase()) {
            case "sedentary" -> 1.2;
            case "lightly" -> 1.375;
            case "moderately" -> 1.55;
            case "very" -> 1.725;
            case "extra" -> 1.9;
            default -> 1.2; // Default to sedentary
        };
    }
}