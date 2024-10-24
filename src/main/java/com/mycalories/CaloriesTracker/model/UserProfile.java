package com.mycalories.CaloriesTracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String goal;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private int heightFeet;

    @Column(nullable = false)
    private int heightInches;

    @Column(nullable = false)
    private double weightLbs;

    @Column(nullable = false)
    private String activityLevel;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    // New fields for BMR calculations
    @Column(name = "maintenance_calories")
    private double maintenanceCalories;

    @Column(name = "gain_calories")
    private double gainCalories;

    @Column(name = "loss_calories")
    private double lossCalories;
}