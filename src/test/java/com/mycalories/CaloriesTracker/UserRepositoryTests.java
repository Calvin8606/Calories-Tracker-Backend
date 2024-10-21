package com.mycalories.CaloriesTracker;

import com.mycalories.CaloriesTracker.model.User;
import com.mycalories.CaloriesTracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository repo;

    @Test
    public void testCreateUser() {
        // Create a User entity object instead of UserDto
        User user = new User();
        user.setEmail("calvin71203@gmail.com");
        user.setPassword("123456789");
        user.setFirstName("Calvin");
        user.setLastName("Chau");
        user.setUsername("Calvin8606");

        // Save the user using the repository
        User savedUser = repo.save(user);

        // Assert that the user was saved and has a non-null ID
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("calvin71203@gmail.com");
    }
}