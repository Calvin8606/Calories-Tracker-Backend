package com.mycalories.CaloriesTracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String goal;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private Integer heightFeet;

    @Column(nullable = false)
    private Integer heightInches;

    @Column(nullable = false)
    private Double weightLbs;

    @Column(nullable = false)
    private String activityLevel;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    @JsonIgnore
    private User user;

    // New fields for BMR calculations
    @Column(name = "maintenance_calories")
    private Double maintenanceCalories;

    @Column(name = "gain_calories")
    private Double gainCalories;

    @Column(name = "loss_calories")
    private Double lossCalories;
}