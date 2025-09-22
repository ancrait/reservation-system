package com.sorokaandriy.reservation_system.reservations.availability;

import com.sorokaandriy.reservation_system.reservations.ReservationController;
import com.sorokaandriy.reservation_system.reservations.ReservationEntity;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations/availability")
public class ReservationsAvailabilityController {

    private final ReservationsAvailabilityService service;
    private static final Logger log = LoggerFactory.getLogger(ReservationsAvailabilityController.class);

    public ReservationsAvailabilityController(ReservationsAvailabilityService service) {
        this.service = service;
    }

    @PostMapping("/check")
    public ResponseEntity<CheckAvailabilityResponse> checkAvailability(
            @RequestBody @Valid CheckAvailabilityRequest request
    )
    {
        log.info("Called method checkAvailability: request={}", request);
        var isAvailable = service.isReservationAvailable(
                request.roomId(),
                request.startDate(),
                request.endDate()
        );
        var message = isAvailable ? "Room available to reservation"
                : "Room not available to reservation";
        var status = isAvailable ? AvailabilityStatus.AVAILABLE
                : AvailabilityStatus.RESERVED;
        if (!isAvailable){
            return ResponseEntity.status(404)
                    .body(new CheckAvailabilityResponse(message, status));
        }
        return ResponseEntity.status(200)
                .body(new CheckAvailabilityResponse(message, status));
    }
}
