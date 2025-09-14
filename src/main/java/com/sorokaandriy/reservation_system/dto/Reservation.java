package com.sorokaandriy.reservation_system.dto;

import java.time.LocalDate;

public record Reservation(
        Long id,
        Long userId,
        Long roomId,
        LocalDate startDate,
        LocalDate endDate,
        ReservationStatus reservationStatus
) {
}
