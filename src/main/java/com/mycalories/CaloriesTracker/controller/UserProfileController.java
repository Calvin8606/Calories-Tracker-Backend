package com.mycalories.CaloriesTracker.controller;

import com.mycalories.CaloriesTracker.dto.UserProfileDto;
import com.mycalories.CaloriesTracker.model.UserProfile;
import com.mycalories.CaloriesTracker.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    // Create or update a user profile
    @PostMapping("/submit")
    public ResponseEntity<UserProfile> createOrUpdateProfile(@RequestBody UserProfileDto profileDto, Principal principal) {
        try {
            UserProfile userProfile = userProfileService.createOrUpdateUserProfile(profileDto, principal);
            return new ResponseEntity<>(userProfile, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get the user profile
    @GetMapping("/get")
    public ResponseEntity<UserProfile> getUserProfile(Principal principal) {
        try {
            UserProfile userProfile = userProfileService.getUserProfile(principal);
            if (userProfile != null) {
                return new ResponseEntity<>(userProfile, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}