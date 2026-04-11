package com.ticketapp.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;
    private String userUid;
    private String userEmail;
    private Integer quantity;
    private String status;

    public Reservation() {}

    public Reservation(Long eventId, String userUid, String userEmail, Integer quantity, String status) {
        this.eventId = eventId;
        this.userUid = userUid;
        this.userEmail = userEmail;
        this.quantity = quantity;
        this.status = status;
    }

    public Long getId() { return id; }
    public Long getEventId() { return eventId; }
    public String getUserUid() { return userUid; }
    public String getUserEmail() { return userEmail; }
    public Integer getQuantity() { return quantity; }
    public String getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public void setUserUid(String userUid) { this.userUid = userUid; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setStatus(String status) { this.status = status; }
}