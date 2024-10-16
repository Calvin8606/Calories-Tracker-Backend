package com.mycalories.CaloriesTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.mycalories.CaloriesTracker.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
}
