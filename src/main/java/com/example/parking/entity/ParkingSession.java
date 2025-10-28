package com.example.parking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class ParkingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vehicle vehicle;

    @OneToOne
    private ParkingSpot parkingSpot;
    private Long checkInTime;
    private Long checkOutTime;
    private String status;
    private Double paidAmount;

    // Constructors, getters, and setters
    public ParkingSession() {
    }       
    public ParkingSession(Long id, Vehicle vehicle, ParkingSpot parkingSpot, Long checkInTime, Long checkOutTime, String status, Double paidAmount) {
        this.id = id;
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
        this.paidAmount = paidAmount;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;  
    }
    public Vehicle getVehicle() {
        return vehicle;
    }
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }
    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }
    public Long getCheckInTime() {
        return checkInTime;
    }
    public void setCheckInTime(Long checkInTime) {
        this.checkInTime = checkInTime;
    }
    public Long getCheckOutTime() {
        return checkOutTime;
    }
    public void setCheckOutTime(Long checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Double getPaidAmount() {
        return paidAmount;
    }
    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }
}
