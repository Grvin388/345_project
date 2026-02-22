package src;

import java.time.LocalDateTime;

public class Reservation {
    private final String reservationId;
    private final String customerId;
    private final String eventId;
    private final LocalDateTime createdAt;
    private final int quantity;
    private ReservationStatus status;

    public Reservation(String reservationId, String customerId, String eventId,
            LocalDateTime createdAt, int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("quantity must be > 0");
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.eventId = eventId;
        this.createdAt = createdAt;
        this.quantity = quantity;
        this.status = ReservationStatus.ACTIVE;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getQuantity() {
        return quantity;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }
}
