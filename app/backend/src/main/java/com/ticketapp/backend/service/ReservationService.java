package com.ticketapp.backend.service;

import com.ticketapp.backend.dto.CreateReservationRequest;
import com.ticketapp.backend.model.Reservation;
import com.ticketapp.backend.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final EmailService emailService;

    public ReservationService(
            ReservationRepository reservationRepository,
            EmailService emailService
    ) {
        this.reservationRepository = reservationRepository;
        this.emailService = emailService;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByUserUid(String userUid) {
        return reservationRepository.findByUserUid(userUid);
    }

    public Reservation createReservation(
            CreateReservationRequest request,
            String userUid,
            String userEmail
    ) {
        Reservation reservation = new Reservation();
        reservation.setEventId(request.getEventId());
        reservation.setQuantity(request.getQuantity());
        reservation.setUserUid(userUid);
        reservation.setUserEmail(userEmail);
        reservation.setStatus("CONFIRMED");

        Reservation savedReservation = reservationRepository.save(reservation);

        if (userEmail != null && !userEmail.isBlank()) {
            try {
                emailService.sendReservationConfirmation(userEmail, savedReservation);
            } catch (Exception e) {
                System.err.println("Failed to send confirmation email: " + e.getMessage());
            }
        } else {
            System.out.println("No email found for user UID: " + userUid);
        }

        return savedReservation;
    }

    public Reservation cancelReservation(Long reservationId, String userUid) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getUserUid().equals(userUid)) {
            throw new RuntimeException("You are not allowed to cancel this reservation");
        }

        reservation.setStatus("CANCELLED");
        return reservationRepository.save(reservation);
    }
}