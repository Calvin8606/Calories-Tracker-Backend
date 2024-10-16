package com.mycalories.CaloriesTracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Required Field")
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    @NotBlank(message = "Required Field")
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    @Column(unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Required Field")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Required Field")
    @Column(nullable = false)
    private String password;
}
