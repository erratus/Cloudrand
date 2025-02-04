package com.cloudrand.arcapi.api.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(name = "username",nullable = false, unique = true)
    private String username;

    @Column(name = "email",nullable = false, unique = true)
    private String email;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password",nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role",nullable = false)
    private Role role = Role.USER;

    @Column(name = "phone",nullable = false, unique = true)
    private String phone;

    @Column(name = "profilePicture",nullable = false)
    private String profilePicture;
}

