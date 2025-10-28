package com.example.parking.dto;

import com.example.parking.Enum.vehicleType;

public class CheckInRequest {
    private String licensePlate;
    private vehicleType vehicleType;

    // Getters and Setters
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