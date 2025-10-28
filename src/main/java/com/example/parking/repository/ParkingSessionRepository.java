package com.example.parking.repository;

import com.example.parking.entity.ParkingSession;
import com.example.parking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {

    // Finds an active parking session for a given vehicle
    Optional<ParkingSession> findByVehicleAndCheckOutTimeIsNull(Vehicle vehicle);
}
