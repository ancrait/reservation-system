package com.sorokaandriy.reservation_system.reservations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {


    @Modifying
    @Query("UPDATE ReservationEntity r set r.reservationStatus = :reservationStatus WHERE r.id = :id")
    void setStatus(@Param("id") Long id, @Param("reservationStatus") ReservationStatus reservationStatus);


    @Query("""
    SELECT r.id FROM ReservationEntity r
        WHERE r.roomId = :roomId
        AND :startDate < r.endDate
        AND r.startDate < :endDate
        AND r.reservationStatus = :reservationStatus""")
    List<Long> findConflictReservationIds(@Param("roomId") Long roomId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate,
                                          @Param("reservationStatus") ReservationStatus reservationStatus);

    @Query("""
    SELECT r FROM ReservationEntity r
        WHERE (:roomId IS NULL OR r.roomId = :roomId) 
        AND (:userId IS NULL OR r.userId = :userId) 
        """)
    List<ReservationEntity> searchAllByFilter(
            @Param("roomId") Long roomId,
            @Param("userId") Long userId,
            Pageable pageable
    );

}
