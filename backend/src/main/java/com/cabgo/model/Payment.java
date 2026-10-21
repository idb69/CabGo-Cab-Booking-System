package com.cabgo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="payments")
public class Payment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long rideId;
    private String riderEmail;
    private double amount;
    private String method;
    private String status = "PAID_DEMO";
    private LocalDateTime createdAt = LocalDateTime.now();
    public Payment() {}
    public Payment(Long rideId,String riderEmail,double amount,String method) {
        this.rideId=rideId;this.riderEmail=riderEmail;this.amount=amount;this.method=method;
    }
    public Long getId(){return id;} public Long getRideId(){return rideId;}
    public String getRiderEmail(){return riderEmail;} public double getAmount(){return amount;}
    public String getMethod(){return method;} public String getStatus(){return status;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
