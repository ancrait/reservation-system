package com.sorokaandriy.reservation_system.reservations;

import com.sorokaandriy.reservation_system.reservations.availability.ReservationsAvailabilityService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {


    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);
    private final AtomicLong idCounter;
    private final ReservationRepository repository;
    private final ReservationMapper mapper;
    private final ReservationsAvailabilityService availabilityService;

    @Autowired
    public ReservationService(ReservationRepository repository, ReservationMapper mapper, ReservationsAvailabilityService availabilityService) {
        this.repository = repository;
        this.mapper = mapper;
        this.availabilityService = availabilityService;
        this.idCounter = new AtomicLong();
    }

    public Reservation getReservationById(Long id) {
        ReservationEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cant find this reservation"));
        return mapper.mapToReservation(entity);
    }


    public List<Reservation> searchAllByFilter(ReservationSearchFilter filter) {
        int pageSize = filter.pageSize() != null ? filter.pageSize() : 10;
        int pageNumber = filter.pageNumber() != null ? filter.pageNumber() : 0;

        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        List<ReservationEntity> reservationEntities = repository.searchAllByFilter(
                filter.roomId(), filter.userId(),pageable
                );
        return reservationEntities.stream()
                .map(mapper::mapToReservation).toList();

    }

    public Reservation saveReservation(Reservation reservation) {
        if (reservation.reservationStatus()!=null){
            throw new IllegalArgumentException("Status should be empty");
        }
        if (!reservation.endDate().isAfter(reservation.startDate())){
            throw new IllegalArgumentException("Start date must be one day earlier than end date");
        }

        ReservationEntity entity = mapper.mapToReservationEntity(reservation);
        return mapper.mapToReservation(repository.save(entity));

    }

    public Reservation update(Long id, Reservation reservation) {
        ReservationEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cant find this reservation"));
        if (!reservation.endDate().isAfter(reservation.startDate())){
            throw new IllegalArgumentException("Start date must be one day earlier than end date");
        }
        if (reservation.reservationStatus() != ReservationStatus.PENDING){
            throw new IllegalArgumentException("Cannot modify reservation with status= " + reservation.reservationStatus());
        }
        mapper.setParamsToEntity(reservation,entity);
        return mapper.mapToReservation(repository.save(entity));

    }
    @Transactional
    public void cancelReservation(Long id, ReservationStatus canceled) {
        var reservation = repository
                .findById(id).orElseThrow(() -> new EntityNotFoundException("Not found reservation by id = " + id));
        if (reservation.getReservationStatus().equals(ReservationStatus.APPROVED)) {
            throw new IllegalStateException("Cannot cancel approved reservation " + id + ". Please contact with manager");
        }
        if (reservation.getReservationStatus().equals(ReservationStatus.CANCELED)) {
            throw new IllegalStateException("Cannot cancel the reservation. Reservation was already canceled");
        }
        repository.setStatus(id,canceled);
    }

    public Reservation approveReservation(Long id) {

        ReservationEntity reservationEntity = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cant find this reservation"));

        if (reservationEntity.getReservationStatus() != ReservationStatus.PENDING){
            throw new IllegalArgumentException("Cant modify reservation status " + reservationEntity.getReservationStatus());
        }

        var isAvailableToApprove = availabilityService.isReservationAvailable(reservationEntity.getRoomId(),
                reservationEntity.getStartDate(),reservationEntity.getEndDate());

        if (!isAvailableToApprove){
            throw new IllegalArgumentException("Cant approve reservation because of conflict");
        }
        reservationEntity.setReservationStatus(ReservationStatus.APPROVED);

        repository.save(reservationEntity);
        return mapper.mapToReservation(reservationEntity);
    }


}
