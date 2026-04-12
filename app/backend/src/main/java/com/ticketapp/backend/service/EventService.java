package com.ticketapp.backend.service;

import com.ticketapp.backend.dto.EventRequest;
import com.ticketapp.backend.model.Event;
import com.ticketapp.backend.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event addEvent(EventRequest request) {
        Event event = new Event(
                request.getTitle(),
                request.getDate(),
                request.getLocation(),
                request.getCategory(),
                "ACTIVE"
        );

        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setTitle(request.getTitle());
        event.setDate(request.getDate());
        event.setLocation(request.getLocation());
        event.setCategory(request.getCategory());

        return eventRepository.save(event);
    }

    public Event cancelEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setStatus("CANCELLED");
        return eventRepository.save(event);
    }
}