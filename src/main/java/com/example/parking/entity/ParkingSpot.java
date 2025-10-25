package com.example.parking.entity;
import com.example.parking.Enum.*;


import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String spotCode;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private spotType spotType;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private spotStatus spotStatus;
    
    //constructors, getters, and setters
    public ParkingSpot() {

    }
    public ParkingSpot(Long id, String spotCode) {
        this.id = id;
        this.spotCode = spotCode;
    }
    public Long getId() {
        return id; 
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSpotCode() {
        return spotCode;
    }
    public void setSpotCode(String spotCode) {
        this.spotCode = spotCode;
    }

    public spotType getType() {
        return spotType;
    }
    public void setType(spotType spotType) {
        this.spotType = spotType;
    }
    public spotStatus getStatus() {
        return spotStatus;
    }
    public void setStatus(spotStatus spotStatus) {
        this.spotStatus = spotStatus;       
    }
    




}
