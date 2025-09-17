package com.sorokaandriy.reservation_system.controller;

import com.sorokaandriy.reservation_system.dto.Reservation;
import com.sorokaandriy.reservation_system.dto.ReservationStatus;
import com.sorokaandriy.reservation_system.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
    public ResponseEntity<List<Reservation>> getAllReservations() {
        log.info("Called method getAllReservations()");
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.findAllReservations());

    }

    @PostMapping
    public ResponseEntity<Reservation> saveReservation(@RequestBody Reservation reservation) {
        log.info("Called method saveReservation()");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.saveReservation(reservation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
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
        log.info("Called method approveReservation()" + id);
        Reservation reservation = reservationService.approveReservation(id);
        return ResponseEntity.ok(reservation);
    }

}
