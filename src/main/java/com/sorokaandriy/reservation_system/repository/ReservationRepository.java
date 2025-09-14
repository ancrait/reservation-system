package com.sorokaandriy.reservation_system.repository;

import com.sorokaandriy.reservation_system.dto.ReservationStatus;
import com.sorokaandriy.reservation_system.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {


    @Modifying
    @Query("UPDATE ReservationEntity r set r.reservationStatus = :reservationStatus WHERE r.id = :id")
    void setStatus(@Param("id") Long id, @Param("reservationStatus") ReservationStatus reservationStatus);

}
