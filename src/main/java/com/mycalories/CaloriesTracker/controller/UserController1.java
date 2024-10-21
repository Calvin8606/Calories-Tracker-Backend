//package com.mycalories.CaloriesTracker.controller;
//
//import com.mycalories.CaloriesTracker.dto.UserDto;
//import com.mycalories.CaloriesTracker.exception.UserAlreadyExistsException;
//import com.mycalories.CaloriesTracker.model.User;
//import com.mycalories.CaloriesTracker.service.UserService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/user")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody @Valid UserDto userDto) {
//        System.out.println("Received user DTO: " + userDto.toString());
//        try {
//            User newUser = userService.registerUser(userDto); // Call the service layer to register the user
//            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
//        } catch (UserAlreadyExistsException e) {
//            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//
//
//
//}
