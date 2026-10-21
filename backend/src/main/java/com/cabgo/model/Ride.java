package com.cabgo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="rides")
public class Ride {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String riderName;
    @Column(nullable=false) private String riderEmail;
    @Column(nullable=false) private String pickup;
    @Column(nullable=false) private String dropLocation;
    @Column(nullable=false) private String cabType;
    private double distanceKm;
    private double fare;
    @Column(nullable=false) private String status = "REQUESTED";
    private String driverName = "Awaiting driver";
    private LocalDateTime createdAt = LocalDateTime.now();
    public Ride() {}
    public Long getId(){return id;} public String getRiderName(){return riderName;}
    public String getRiderEmail(){return riderEmail;} public String getPickup(){return pickup;}
    public String getDropLocation(){return dropLocation;} public String getCabType(){return cabType;}
    public double getDistanceKm(){return distanceKm;} public double getFare(){return fare;}
    public String getStatus(){return status;} public String getDriverName(){return driverName;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setRiderName(String v){riderName=v;} public void setRiderEmail(String v){riderEmail=v;}
    public void setPickup(String v){pickup=v;} public void setDropLocation(String v){dropLocation=v;}
    public void setCabType(String v){cabType=v;} public void setDistanceKm(double v){distanceKm=v;}
    public void setFare(double v){fare=v;} public void setStatus(String v){status=v;}
    public void setDriverName(String v){driverName=v;}
}
