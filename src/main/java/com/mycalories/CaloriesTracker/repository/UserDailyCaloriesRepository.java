package com.mycalories.CaloriesTracker.repository;

import com.mycalories.CaloriesTracker.model.User;
import com.mycalories.CaloriesTracker.model.UserDailyCalories;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserDailyCaloriesRepository extends JpaRepository<UserDailyCalories, Long> {

    Optional<UserDailyCalories> findByDate(LocalDate date);

    List<UserDailyCalories> dateBetween(LocalDate startDate, LocalDate endDate);
}