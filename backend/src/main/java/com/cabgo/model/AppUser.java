package com.cabgo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="app_users")
public class AppUser {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(nullable=false, unique=true) private String email;
    @Column(nullable=false) private String passwordHash;
    @Column(nullable=false) private String role = "RIDER";
    private LocalDateTime createdAt = LocalDateTime.now();
    public AppUser() {}
    public AppUser(String name, String email, String passwordHash, String role) {
        this.name=name; this.email=email; this.passwordHash=passwordHash; this.role=role;
    }
    public Long getId(){return id;} public String getName(){return name;}
    public String getEmail(){return email;} public String getPasswordHash(){return passwordHash;}
    public String getRole(){return role;} public LocalDateTime getCreatedAt(){return createdAt;}
    public void setName(String v){name=v;} public void setEmail(String v){email=v;}
    public void setPasswordHash(String v){passwordHash=v;} public void setRole(String v){role=v;}
}
