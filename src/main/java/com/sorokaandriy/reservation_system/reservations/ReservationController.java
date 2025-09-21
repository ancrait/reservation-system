package com.sorokaandriy.reservation_system.reservations;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        log.info("Called method getReservationById() with id {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.getReservationById(id));
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations(
            @RequestParam(name = "roomId",required = false) Long roomId,
            @RequestParam(name = "userId",required = false) Long userId,
            @RequestParam(name = "pageSize",required = false) Integer pageSize,
            @RequestParam(name = "pageNumber",required = false) Integer pageNumber
    ) {
        log.info("Called method getAllReservations()");
        var filter = new ReservationSearchFilter(roomId,userId,pageSize,pageNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.searchAllByFilter(filter));

    }

    @PostMapping
    public ResponseEntity<Reservation> saveReservation(@RequestBody @Valid Reservation reservation) {
        log.info("Called method saveReservation()");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.saveReservation(reservation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody @Valid Reservation reservation) {
        log.info("Called method updateReservation()");
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.update(id, reservation));
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        log.info("Called method deleteReservation()");
        reservationService.cancelReservation(id, ReservationStatus.CANCELED);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Reservation> approveReservation(@PathVariable Long id) {
        log.info("Called method approveReservation() " + id);
        Reservation reservation = reservationService.approveReservation(id);
        return ResponseEntity.ok(reservation);
    }

}
