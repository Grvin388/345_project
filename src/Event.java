package src;

import java.time.LocalDateTime;

public class Event {
    private final String eventId;
    private String title;
    private LocalDateTime dateTime;
    private String location;
    private String category;
    private EventStatus status;
    private int capacity;
    private int availableSeats;

    public Event(String eventId, String title, LocalDateTime dateTime,
            String location, String category, int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("capacity must be > 0");
        this.eventId = eventId;
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.category = category;
        this.capacity = capacity;
        this.availableSeats = capacity;
        this.status = EventStatus.ACTIVE;
    }

    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public EventStatus getStatus() {
        return status;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    /** Reserve {@code quantity} seats; throws if not enough seats or event is not ACTIVE. */
    public void reserveSeats(int quantity) {
        if (status != EventStatus.ACTIVE)
            throw new IllegalStateException("Event is not active");
        if (quantity > availableSeats)
            throw new IllegalStateException("Not enough available seats");
        availableSeats -= quantity;
    }

    /** Release {@code quantity} seats back to the pool. */
    public void releaseSeats(int quantity) {
        availableSeats = Math.min(capacity, availableSeats + quantity);
    }

    /** Cancel the event, making it unavailable for new reservations. */
    public void cancel() {
        this.status = EventStatus.CANCELLED;
    }
}
