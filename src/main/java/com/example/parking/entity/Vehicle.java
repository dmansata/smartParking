package com.example.parking.entity;
import com.example.parking.Enum.vehicleType;

import jakarta.persistence.Enumerated;

public class Vehicle {
    private long id;
    private String licensePlate;

    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private vehicleType vehicleType;

    // Constructors, getters, and setters
    public Vehicle() {
    }   
    public Vehicle(long id, String licensePlate, vehicleType vehicleType) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
    }
    public long getId() {
        return id;      
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getLicensePlate() {
        return licensePlate;
    }
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    public vehicleType getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(vehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }   
}
