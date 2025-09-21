package com.sorokaandriy.reservation_system.reservations;

import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public Reservation mapToReservation(ReservationEntity entity){
        return new Reservation(entity.getId(),
                entity.getUserId(),
                entity.getRoomId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getReservationStatus());
    }

    public ReservationEntity mapToReservationEntity(Reservation reservation){
        return new ReservationEntity(
                null,
                reservation.userId(),
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                ReservationStatus.PENDING);
    }

    public ReservationEntity setParamsToEntity(Reservation reservation,ReservationEntity entity){
        entity.setUserId(reservation.userId());
        entity.setRoomId(reservation.roomId());
        entity.setStartDate(reservation.startDate());
        entity.setEndDate(reservation.endDate());
        entity.setReservationStatus(ReservationStatus.PENDING);
        return entity;
    }
}
