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
        this.eventId = eventId;
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.category = category;
        this.capacity = capacity;
        this.availableSeats = capacity;
        this.status = EventStatus.ACTIVE;
    }
}
