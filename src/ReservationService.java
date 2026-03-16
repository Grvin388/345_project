package src;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Core business-logic service for creating and cancelling reservations.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Validate seat availability before booking</li>
 *   <li>Update the event's available-seat count</li>
 *   <li>Persist reservations in an in-memory store</li>
 *   <li>Trigger confirmation / cancellation notifications via {@link NotificationService}</li>
 * </ul>
 */
public class ReservationService {

    private final Map<String, Reservation> reservations = new HashMap<>();
    private final NotificationService notificationService;

    public ReservationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Make a reservation for {@code customer} on {@code event} for {@code quantity} seats.
     *
     * @return the newly created {@link Reservation}
     * @throws IllegalArgumentException if quantity is not positive
     * @throws IllegalStateException    if the event is not active or there are not enough seats
     */
    public Reservation makeReservation(Customer customer, Event event, int quantity) {
        event.reserveSeats(quantity); // validates status and availability

        String reservationId = UUID.randomUUID().toString();
        Reservation reservation = new Reservation(
                reservationId,
                customer.getUserID(),
                event.getEventId(),
                LocalDateTime.now(),
                quantity);

        reservations.put(reservationId, reservation);

        notificationService.sendConfirmation(customer, reservation);
        return reservation;
    }

    /**
     * Cancel an existing reservation, restoring the seats to the event.
     *
     * @param reservationId ID of the reservation to cancel
     * @param event         the event the reservation belongs to (used to restore seats)
     * @param customer      the customer who owns the reservation (used for notification)
     * @throws IllegalArgumentException if the reservation is not found
     * @throws IllegalStateException    if the reservation is already cancelled
     */
    public void cancelReservation(String reservationId, Event event, Customer customer) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null)
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        if (reservation.getStatus() == ReservationStatus.CANCELLED)
            throw new IllegalStateException("Reservation is already cancelled");

        reservation.cancel();
        event.releaseSeats(reservation.getQuantity());

        notificationService.sendCancellation(customer, reservation);
    }

    /** Return an unmodifiable view of all reservations. */
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(new ArrayList<>(reservations.values()));
    }

    /**
     * Return all active reservations for a given customer.
     *
     * @param customerId the customer's ID
     */
    public List<Reservation> getReservationsForCustomer(String customerId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations.values()) {
            if (r.getCustomerId().equals(customerId)
                    && r.getStatus() == ReservationStatus.ACTIVE) {
                result.add(r);
            }
        }
        return Collections.unmodifiableList(result);
    }
}
