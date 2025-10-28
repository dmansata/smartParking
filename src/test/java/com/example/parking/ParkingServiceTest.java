package com.example.parking.service;

import com.example.parking.Enum.spotStatus;
import com.example.parking.Enum.spotType;
import com.example.parking.Enum.vehicleType;
import com.example.parking.entity.ParkingSpot;
import com.example.parking.entity.ParkingSession;
import com.example.parking.entity.Vehicle;
import com.example.parking.exception.ConflictException;
import com.example.parking.exception.ResourceNotFoundException;
import com.example.parking.repository.ParkingSpotRepository;
import com.example.parking.repository.ParkingSessionRepository;
import com.example.parking.repository.VehicleRepository;
import com.example.parking.service.ParkingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Mock
    private ParkingSessionRepository parkingSessionRepository;

    @InjectMocks
    private ParkingService parkingService;

    private Vehicle vehicle;
    private ParkingSpot parkingSpot;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setLicensePlate("TEST-123");
        vehicle.setVehicleType(vehicleType.CAR);

        parkingSpot = new ParkingSpot();
        parkingSpot.setId(1L);
        parkingSpot.setSpotCode("R-01");
        parkingSpot.setType(spotType.REGULAR);
        parkingSpot.setStatus(spotStatus.AVAILABLE);
    }

    @Test
    void checkIn_Success_NewVehicle() {
        // Arrange
        when(vehicleRepository.findByLicensePlate("TEST-123")).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(parkingSessionRepository.findByVehicleAndCheckOutTimeIsNull(any(Vehicle.class))).thenReturn(Optional.empty());
        when(parkingSpotRepository.findFirstBySpotTypeAndSpotStatus(spotType.REGULAR, spotStatus.AVAILABLE)).thenReturn(Optional.of(parkingSpot));
        when(parkingSessionRepository.save(any(ParkingSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ParkingSession session = parkingService.checkIn("TEST-123", vehicleType.CAR);

        // Assert
        assertNotNull(session);
        assertEquals("ACTIVE", session.getStatus());
        assertEquals(vehicle, session.getVehicle());
        assertEquals(parkingSpot, session.getParkingSpot());
        assertEquals(spotStatus.OCCUPIED, parkingSpot.getStatus());

        verify(vehicleRepository, times(1)).findByLicensePlate("TEST-123");
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
        verify(parkingSpotRepository, times(1)).save(parkingSpot);
        verify(parkingSessionRepository, times(1)).save(any(ParkingSession.class));
    }

    @Test
    void checkIn_Fail_VehicleAlreadyParked() {
        // Arrange
        when(vehicleRepository.findByLicensePlate("TEST-123")).thenReturn(Optional.of(vehicle));
        when(parkingSessionRepository.findByVehicleAndCheckOutTimeIsNull(vehicle)).thenReturn(Optional.of(new ParkingSession()));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            parkingService.checkIn("TEST-123", vehicleType.CAR);
        });

        assertEquals("Vehicle with license plate TEST-123 is already parked.", exception.getMessage());
        verify(parkingSpotRepository, never()).findFirstBySpotTypeAndSpotStatus(any(), any());
    }

    @Test
    void checkIn_Fail_NoSpotsAvailable() {
        // Arrange
        when(vehicleRepository.findByLicensePlate("TEST-123")).thenReturn(Optional.of(vehicle));
        when(parkingSessionRepository.findByVehicleAndCheckOutTimeIsNull(vehicle)).thenReturn(Optional.empty());
        when(parkingSpotRepository.findFirstBySpotTypeAndSpotStatus(spotType.REGULAR, spotStatus.AVAILABLE)).thenReturn(Optional.empty());

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            parkingService.checkIn("TEST-123", vehicleType.CAR);
        });

        assertEquals("No available REGULAR spots.", exception.getMessage());
    }

    @Test
    void checkOut_Success() {
        // Arrange
        ParkingSession activeSession = new ParkingSession();
        activeSession.setVehicle(vehicle);
        activeSession.setParkingSpot(parkingSpot);
        activeSession.setCheckInTime(System.currentTimeMillis() - 3600_000); // 1 hour ago
        parkingSpot.setStatus(spotStatus.OCCUPIED);

        when(vehicleRepository.findByLicensePlate("TEST-123")).thenReturn(Optional.of(vehicle));
        when(parkingSessionRepository.findByVehicleAndCheckOutTimeIsNull(vehicle)).thenReturn(Optional.of(activeSession));
        when(parkingSessionRepository.save(any(ParkingSession.class))).thenReturn(activeSession);

        // Act
        ParkingSession session = parkingService.checkOut("TEST-123");

        // Assert
        assertNotNull(session.getCheckOutTime());
        assertEquals("COMPLETED", session.getStatus());
        assertEquals(spotStatus.AVAILABLE, parkingSpot.getStatus());

        verify(parkingSpotRepository, times(1)).save(parkingSpot);
        verify(parkingSessionRepository, times(1)).save(activeSession);
    }

    @Test
    void checkOut_Fail_NoActiveSession() {
        // Arrange
        when(vehicleRepository.findByLicensePlate("TEST-123")).thenReturn(Optional.of(vehicle));
        when(parkingSessionRepository.findByVehicleAndCheckOutTimeIsNull(vehicle)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            parkingService.checkOut("TEST-123");
        });

        assertEquals("No active parking session found for vehicle with license plate TEST-123.", exception.getMessage());
        verify(parkingSpotRepository, never()).save(any());
    }
}