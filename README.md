
# MyCaloriesTracker - Back End

The back end of **MyCaloriesTracker** is a Spring Boot application providing API endpoints for user management, calorie tracking, and authentication.

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Setup Instructions](#setup-instructions)
- [Folder Structure](#folder-structure)
- [API Documentation](#api-documentation)
- [Usage](#usage)

---

## Features

- **User Registration and Authentication**: Register and log in users with JWT-based authentication.
- **Profile Management**: Update user details, including phone number and password.
- **Calories Tracking**: Allows users to log food items and track daily calorie intake.

## Tech Stack

- **Spring Boot** for the REST API
- **Spring Security** for authentication and authorization
- **JWT** for secure, stateless authentication
- **MySQL** for database (configurable)
- **NutrinixAPI** Link: https://www.nutritionix.com/business/api

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone <your-backend-repo-url>
   cd <backend-directory>
   ```

2. **Database Configuration**:
   - Open `src/main/resources/application.properties` and update the database details:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/mycaloriestracker
     spring.datasource.username=your-db-username
     spring.datasource.password=your-db-password
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Run the Application**:
   - Run the application from your IDE or use:
     ```bash
     ./mvnw spring-boot:run
     ```

   The back end will run on `http://localhost:8080`.

## Folder Structure

- `com.mycalories.CaloriesTracker.controller`: REST controllers for handling API requests.
- `com.mycalories.CaloriesTracker.service`: Service layer for business logic.
- `com.mycalories.CaloriesTracker.model`: Entity classes representing the database.
- `com.mycalories.CaloriesTracker.repository`: Data access layer with repository interfaces.

## API Documentation

### User Authentication
- **POST** `/api/user/register` - Register a new user.
- **POST** `/api/user/login` - Log in a user and receive a JWT token.

### Profile Management
- **GET** `/api/user/details` - Fetch user details.
- **PUT** `/api/user/update-phone` - Update the user’s phone number.
- **PUT** `/api/user/update-password` - Update the user’s password.

### Calories Tracking
- **POST** `/api/calories/date/{date}/addFood` - Add a food entry for a specific date.
- **DELETE** `/api/calories/food/{foodEntryId}` - Delete a food entry.
- **GET** `/api/calories/date/{date}` - Retrieve total calories and food items for a specific date.

## Usage

1. **User Registration and Login**: Users can sign up and log in to receive a JWT token for secure API access.
2. **Profile Management**: Users can view and update their phone number and password.
3. **Calories Tracking**: Users can log daily food intake, which is stored in the database and retrievable by date.

---
