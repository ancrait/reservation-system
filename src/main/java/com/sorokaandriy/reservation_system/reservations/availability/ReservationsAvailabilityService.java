package com.sorokaandriy.reservation_system.reservations.availability;

import com.sorokaandriy.reservation_system.reservations.ReservationRepository;
import com.sorokaandriy.reservation_system.reservations.ReservationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationsAvailabilityService {

    private final ReservationRepository repository;
    private static final Logger log = LoggerFactory.getLogger(ReservationsAvailabilityService.class);

    public ReservationsAvailabilityService(ReservationRepository repository) {
        this.repository = repository;
    }

    public boolean isReservationAvailable(Long roomId, LocalDate startDate,
                                          LocalDate endDate){
        if (!endDate.isAfter(startDate)){
            throw new IllegalArgumentException("Start date must be one day earlier than end date");
        }
        List<Long> conflictingIds = repository.findConflictReservationIds(roomId,startDate,endDate, ReservationStatus.APPROVED);
        if (conflictingIds.isEmpty()){
            return true;
        }
        log.info("Conflict with ids={}", conflictingIds);
        return false;
    }

}
