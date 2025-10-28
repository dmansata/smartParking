package com.example.parking.entity;
import com.example.parking.Enum.vehicleType;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Vehicle {
    private long id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String licensePlate;

    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private vehicleType vehicleType;

    // Constructors, getters, and setters
    public Vehicle() {
    }   
    public Vehicle(Long id, String licensePlate, vehicleType vehicleType) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
    }
    public Long getId() {
        return id;      
    }
    public void setId(Long id) {
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
