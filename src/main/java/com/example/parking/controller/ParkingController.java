package com.example.parking.controller;

import com.example.parking.dto.CheckInRequest;
import com.example.parking.entity.ParkingSession;
import com.example.parking.service.ParkingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    /**
     * Handles the check-in of a vehicle.
     * POST /api/v1/parking/check-in
     * Body: { "licensePlate": "ABC-123", "vehicleType": "CAR" }
     */
    @PostMapping("/check-in")
    public ResponseEntity<ParkingSession> checkIn(@RequestBody CheckInRequest checkInRequest) {
        ParkingSession session = parkingService.checkIn(checkInRequest.getLicensePlate(), checkInRequest.getVehicleType());
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    /**
     * Handles the check-out of a vehicle.
     * POST /api/v1/parking/check-out/{licensePlate}
     */
    @PostMapping("/check-out/{licensePlate}")
    public ResponseEntity<ParkingSession> checkOut(@PathVariable String licensePlate) {
        ParkingSession session = parkingService.checkOut(licensePlate);
        return ResponseEntity.ok(session);
    }
}
