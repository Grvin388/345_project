package com.ticketapp.backend.service;

import com.ticketapp.backend.dto.EventRequest;
import com.ticketapp.backend.model.Event;
import com.ticketapp.backend.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private EventRequest buildRequest(String title, String date, String location, String category) {
        EventRequest r = new EventRequest();
        r.setTitle(title);
        r.setDate(date);
        r.setLocation(location);
        r.setCategory(category);
        return r;
    }

    @Test
    void getAllEvents_returnsAllEventsFromRepository() {
        List<Event> events = List.of(
                new Event("Concert A", "2026-05-01", "Montreal", "Music", "ACTIVE"),
                new Event("Concert B", "2026-06-01", "Toronto", "Jazz", "ACTIVE"));
        when(eventRepository.findAll()).thenReturn(events);

        List<Event> result = eventService.getAllEvents();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Concert A");
    }

    @Test
    void addEvent_savesEventWithActiveStatus() {
        EventRequest request = buildRequest("Concert", "2026-05-01", "Montreal", "Music");
        Event saved = new Event("Concert", "2026-05-01", "Montreal", "Music", "ACTIVE");
        when(eventRepository.save(any(Event.class))).thenReturn(saved);

        Event result = eventService.addEvent(request);

        assertThat(result.getStatus()).isEqualTo("ACTIVE");
        assertThat(result.getTitle()).isEqualTo("Concert");
    }

    @Test
    void updateEvent_updatesAllFieldsAndSaves() {
        Event existing = new Event("Old Title", "2026-01-01", "Old City", "Old Cat", "ACTIVE");
        EventRequest request = buildRequest("New Title", "2026-05-01", "New City", "New Cat");
        Event updated = new Event("New Title", "2026-05-01", "New City", "New Cat", "ACTIVE");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(eventRepository.save(any(Event.class))).thenReturn(updated);

        Event result = eventService.updateEvent(1L, request);

        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getLocation()).isEqualTo("New City");
        assertThat(result.getCategory()).isEqualTo("New Cat");
    }

    @Test
    void updateEvent_throwsWhenEventNotFound() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.updateEvent(99L, buildRequest("T", "D", "L", "C")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Event not found");
    }

    @Test
    void cancelEvent_setsStatusToCancelled() {
        Event event = new Event("Concert", "2026-05-01", "Montreal", "Music", "ACTIVE");
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));

        Event result = eventService.cancelEvent(1L);

        assertThat(result.getStatus()).isEqualTo("CANCELLED");
    }

    @Test
    void cancelEvent_throwsWhenEventNotFound() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.cancelEvent(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Event not found");
    }
}
