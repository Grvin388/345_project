package com.ticketapp.backend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ticketapp.backend.repository.EventRepository;

@SpringBootTest
@ActiveProfiles("test")
class EventIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        eventRepository.deleteAll();
    }

    @Test
    void createEvent_returnsEventWithActiveStatus() throws Exception {
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Jazz Fest\",\"date\":\"2026-07-01\",\"location\":\"Montreal\",\"category\":\"Music\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Jazz Fest"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void createEvent_thenGetAll_returnsIt() throws Exception {
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Jazz Fest\",\"date\":\"2026-07-01\",\"location\":\"Montreal\",\"category\":\"Music\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Jazz Fest"));
    }

    @Test
    void cancelEvent_changesStatusToCancelled() throws Exception {
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Jazz Fest\",\"date\":\"2026-07-01\",\"location\":\"Montreal\",\"category\":\"Music\"}"))
                .andExpect(status().isOk());

        Long id = eventRepository.findAll().get(0).getId();

        mockMvc.perform(patch("/api/events/" + id + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void updateEvent_withValidId_updatesFields() throws Exception {
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Old Title\",\"date\":\"2026-07-01\",\"location\":\"Montreal\",\"category\":\"Music\"}"))
                .andExpect(status().isOk());

        Long id = eventRepository.findAll().get(0).getId();

        mockMvc.perform(put("/api/events/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New Title\",\"date\":\"2026-09-01\",\"location\":\"Toronto\",\"category\":\"Conference\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.location").value("Toronto"));
    }

    @Test
    void updateEvent_withInvalidId_throwsRuntimeException() {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () ->
            mockMvc.perform(put("/api/events/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New\",\"date\":\"2026-07-01\",\"location\":\"NYC\",\"category\":\"Music\"}")));
    }
}
