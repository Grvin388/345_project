package com.ticketapp.backend.repository;

import com.ticketapp.backend.model.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void findByUserUid_returnsOnlyReservationsForThatUser() {
        reservationRepository.save(new Reservation(1L, "uid-alice", "alice@test.com", 2, "CONFIRMED"));
        reservationRepository.save(new Reservation(2L, "uid-alice", "alice@test.com", 1, "CANCELLED"));
        reservationRepository.save(new Reservation(3L, "uid-bob", "bob@test.com", 3, "CONFIRMED"));

        List<Reservation> result = reservationRepository.findByUserUid("uid-alice");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getUserUid().equals("uid-alice"));
    }

    @Test
    void findByUserUid_returnsEmptyListWhenUserHasNoReservations() {
        reservationRepository.save(new Reservation(1L, "uid-alice", "alice@test.com", 2, "CONFIRMED"));

        List<Reservation> result = reservationRepository.findByUserUid("uid-unknown");

        assertThat(result).isEmpty();
    }

    @Test
    void save_persistsAllReservationFields() {
        Reservation reservation = new Reservation(5L, "uid-carol", "carol@test.com", 4, "CONFIRMED");

        Reservation saved = reservationRepository.save(reservation);

        assertThat(saved.getId()).isNotNull();
        assertEquals(5L, saved.getEventId());
        assertEquals("uid-carol", saved.getUserUid());
        assertEquals("carol@test.com", saved.getUserEmail());
        assertEquals(4, saved.getQuantity());
        assertEquals("CONFIRMED", saved.getStatus());
    }
}
