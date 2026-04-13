package com.ticketapp.backend.controller;

import tools.jackson.databind.ObjectMapper;
import com.ticketapp.backend.config.FirebaseConfig;
import com.ticketapp.backend.dto.EventRequest;
import com.ticketapp.backend.model.Event;
import com.ticketapp.backend.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    // Prevent FirebaseConfig @PostConstruct from running in the MVC slice context
    @MockitoBean
    private FirebaseConfig firebaseConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllEvents_returns200WithEventList() throws Exception {
        Event event = new Event("Concert", "2026-05-01", "Montreal", "Music", "ACTIVE");
        when(eventService.getAllEvents()).thenReturn(List.of(event));

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Concert"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    void getAllEvents_returns200WithEmptyListWhenNoEvents() throws Exception {
        when(eventService.getAllEvents()).thenReturn(List.of());

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void addEvent_returns200WithCreatedEvent() throws Exception {
        EventRequest request = new EventRequest();
        request.setTitle("Jazz Night");
        request.setDate("2026-06-01");
        request.setLocation("Quebec City");
        request.setCategory("Jazz");

        Event saved = new Event("Jazz Night", "2026-06-01", "Quebec City", "Jazz", "ACTIVE");
        when(eventService.addEvent(any(EventRequest.class))).thenReturn(saved);

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Jazz Night"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void updateEvent_returns200WithUpdatedEvent() throws Exception {
        EventRequest request = new EventRequest();
        request.setTitle("Updated Title");
        request.setDate("2026-07-01");
        request.setLocation("Ottawa");
        request.setCategory("Pop");

        Event updated = new Event("Updated Title", "2026-07-01", "Ottawa", "Pop", "ACTIVE");
        when(eventService.updateEvent(eq(1L), any(EventRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/api/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.location").value("Ottawa"));
    }

    @Test
    void cancelEvent_returns200WithCancelledStatus() throws Exception {
        Event cancelled = new Event("Concert", "2026-05-01", "Montreal", "Music", "CANCELLED");
        when(eventService.cancelEvent(1L)).thenReturn(cancelled);

        mockMvc.perform(patch("/api/events/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}
