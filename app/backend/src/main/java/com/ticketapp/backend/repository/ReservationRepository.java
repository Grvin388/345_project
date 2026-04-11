package com.ticketapp.backend.repository;

import com.ticketapp.backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserUid(String userUid);
}