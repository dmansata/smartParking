package com.example.parking.service;

import com.example.parking.Enum.spotStatus;
import com.example.parking.Enum.spotType;
import com.example.parking.Enum.vehicleType;
import com.example.parking.entity.ParkingSpot;
import com.example.parking.entity.ParkingSession;
import com.example.parking.entity.Vehicle;
import com.example.parking.repository.ParkingSpotRepository;
import com.example.parking.repository.ParkingSessionRepository;
import com.example.parking.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ParkingService {

    private final VehicleRepository vehicleRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingSessionRepository parkingSessionRepository;

    public ParkingService(VehicleRepository vehicleRepository,
                          ParkingSpotRepository parkingSpotRepository,
                          ParkingSessionRepository parkingSessionRepository) {
        this.vehicleRepository = vehicleRepository;
        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingSessionRepository = parkingSessionRepository;
    }

    @Transactional
    public ParkingSession checkIn(String licensePlate, vehicleType vehicleType) {
        // 1. Find or create the vehicle
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseGet(() -> {
                    Vehicle newVehicle = new Vehicle();
                    newVehicle.setLicensePlate(licensePlate);
                    newVehicle.setVehicleType(vehicleType);
                    return vehicleRepository.save(newVehicle);
                });

        // 2. Check if the vehicle is already parked
        parkingSessionRepository.findByVehicleAndCheckOutTimeIsNull(vehicle)
                .ifPresent(session -> {
                    throw new IllegalStateException("Vehicle with license plate " + licensePlate + " is already parked.");
                });

        // 3. Determine the required spot type and find an available spot
        spotType requiredSpotType = getSpotTypeForVehicle(vehicleType);
        ParkingSpot availableSpot = parkingSpotRepository.findFirstBySpotTypeAndSpotStatus(requiredSpotType, spotStatus.AVAILABLE)
                .orElseThrow(() -> new IllegalStateException("No available " + requiredSpotType + " spots."));

        // 4. Update the spot status to OCCUPIED
        availableSpot.setStatus(spotStatus.OCCUPIED);
        parkingSpotRepository.save(availableSpot);

        // 5. Create and save the new parking session
        ParkingSession newSession = new ParkingSession();
        newSession.setVehicle(vehicle);
        newSession.setParkingSpot(availableSpot);
        newSession.setCheckInTime(Instant.now().toEpochMilli());
        newSession.setStatus("ACTIVE");

        return parkingSessionRepository.save(newSession);
    }

    @Transactional
    public ParkingSession checkOut(String licensePlate) {
        // 1. Find the vehicle
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new IllegalStateException("Vehicle with license plate " + licensePlate + " not found."));

        // 2. Find the active parking session
        ParkingSession activeSession = parkingSessionRepository.findByVehicleAndCheckOutTimeIsNull(vehicle)
                .orElseThrow(() -> new IllegalStateException("No active parking session found for vehicle with license plate " + licensePlate + "."));
        // 3. Update the parking session with check-out time and status
        activeSession.setCheckOutTime(Instant.now().toEpochMilli());
        activeSession.setStatus("COMPLETED");

        // 4. Update the spot status to AVAILABLE
        ParkingSpot occupiedSpot = activeSession.getParkingSpot();
        occupiedSpot.setStatus(spotStatus.AVAILABLE);
        parkingSpotRepository.save(occupiedSpot);

        return parkingSessionRepository.save(activeSession);
    }

    private spotType getSpotTypeForVehicle(vehicleType vType) {
        return switch (vType) {
            case MOTORCYCLE -> spotType.COMPACT;
            case CAR, SUV -> spotType.REGULAR;
            case TRUCK -> spotType.LARGE;
        };
    }



}
