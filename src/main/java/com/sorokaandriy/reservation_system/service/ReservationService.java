package com.sorokaandriy.reservation_system.service;

import com.sorokaandriy.reservation_system.dto.Reservation;
import com.sorokaandriy.reservation_system.entity.ReservationEntity;
import com.sorokaandriy.reservation_system.dto.ReservationStatus;
import com.sorokaandriy.reservation_system.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {

    private final Map<Long,Reservation> reservationsMap;
    private final AtomicLong idCounter;
    private final ReservationRepository repository;

    @Autowired
    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
        this.reservationsMap = new HashMap<>();
        this.idCounter = new AtomicLong();
    }

    public Reservation getReservationById(Long id) {
        ReservationEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cant find this reservation"));
        return mapToReservation(entity);
    }


    public List<Reservation> findAllReservations() {
        List<ReservationEntity> reservationEntities = repository.findAll();
        return reservationEntities.stream()
                .map(el ->
                        new Reservation(
                                el.getId(),
                                el.getUserId(),
                                el.getRoomId(),
                                el.getStartDate(),
                                el.getEndDate(),
                                el.getReservationStatus()
                        )).toList();

    }

    public Reservation saveReservation(Reservation reservation) {
        if (reservation.id()!=null){
            throw new IllegalArgumentException("Id should be empty");
        }
        if (reservation.reservationStatus()!=null){
            throw new IllegalArgumentException("Status should be empty");
        }
        ReservationEntity entity = mapToReservationEntity(reservation);
        return mapToReservation(repository.save(entity));

    }

    public Reservation update(Long id, Reservation reservation) {
        if (!repository.existsById(id)){
            throw new NoSuchElementException("Not found reservation by id " + id);
        }
        ReservationEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cant find this reservation"));
        setParamsToEntity(reservation,entity);
        return mapToReservation(repository.save(entity));

    }
    @Transactional
    public void cancelReservation(Long id, ReservationStatus canceled) {
        if (!repository.existsById(id)){
            throw new EntityNotFoundException("Not found reservation by id " + id);
        }
        repository.setStatus(id,canceled);

    }

    public Reservation approveReservation(Long id) {

        ReservationEntity reservationEntity = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cant find this reservation"));

        if (reservationEntity.getReservationStatus() != ReservationStatus.PENDING){
            throw new IllegalArgumentException("Cant modify reservation status " + reservationEntity.getReservationStatus());
        }
        var isConflict = isReservationConflict(reservationEntity);
        if (isConflict){
            throw new IllegalArgumentException("Cant approve reservation because of conflict ");
        }
        reservationEntity.setReservationStatus(ReservationStatus.APPROVED);

        repository.save(reservationEntity);
        return mapToReservation(reservationEntity);
    }

    private boolean isReservationConflict(ReservationEntity reservationEntity){
        for (ReservationEntity exReservation : repository.findAll()){
                if (reservationEntity.getRoomId().equals(exReservation.getRoomId())) {
                    if (exReservation.getReservationStatus().equals(ReservationStatus.APPROVED)) {
                        if (reservationEntity.getStartDate().isBefore(exReservation.getEndDate())
                                && exReservation.getStartDate().isBefore(reservationEntity.getEndDate())) {
                            return true;
                        }
                    }

            }
        }
        return false;
    }

    private Reservation mapToReservation(ReservationEntity entity){
        return new Reservation(entity.getId(),
                entity.getUserId(),
                entity.getRoomId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getReservationStatus());
    }

    private ReservationEntity mapToReservationEntity(Reservation reservation){
        return new ReservationEntity(
                null,
                reservation.userId(),
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                ReservationStatus.PENDING);
    }

    private ReservationEntity setParamsToEntity(Reservation reservation,ReservationEntity entity){
        entity.setUserId(reservation.userId());
        entity.setRoomId(reservation.roomId());
        entity.setStartDate(reservation.startDate());
        entity.setEndDate(reservation.endDate());
        entity.setReservationStatus(ReservationStatus.PENDING);
        return entity;
    }

}
