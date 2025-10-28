package com.example.parking.repository;

import com.example.parking.Enum.spotStatus;
import com.example.parking.Enum.spotType;
import com.example.parking.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long>  {

    // Finds the first available parking spot of a given type
    Optional<ParkingSpot> findFirstBySpotTypeAndSpotStatus(spotType spotType, spotStatus spotStatus);
}
