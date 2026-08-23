package com.abdellah.spring_auth_starter.entity;


import com.abdellah.spring_auth_starter.enums.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Email
    @Column(nullable = false, name = "email", unique = true)
    @Size(min = 5, max = 60 , message = "Email length must be between 5 and 60")
    @NotBlank
    private String email;

    @JsonIgnore
    @Column(nullable = false, name="password")
    @Size(min = 8 , max = 30 , message = "Password must be between 8 and 30")
    @NotBlank
    private String password;


    @Enumerated(EnumType.STRING)
    @NotBlank
    private USER_ROLE role;

    @Column(name = "created_at",nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;


    @PrePersist
    protected void onCreate(){
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updated_at = LocalDateTime.now();
    }

}
