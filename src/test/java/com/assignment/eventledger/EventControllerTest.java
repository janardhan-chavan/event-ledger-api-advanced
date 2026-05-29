package com.assignment.eventledger;

import com.assignment.eventledger.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Should create event successfully")
    void shouldCreateEventSuccessfully() throws Exception {

        String request = """
                {
                  "eventId": "evt-001",
                  "accountId": "acct-123",
                  "type": "CREDIT",
                  "amount": 150.00,
                  "currency": "USD",
                  "eventTimestamp": "2026-05-15T14:02:11Z",
                  "metadata": "mainframe-batch"
                }
                """;

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return 200 for duplicate event submission")
    void shouldHandleDuplicateEvent() throws Exception {

        String request = """
                {
                  "eventId": "evt-001",
                  "accountId": "acct-123",
                  "type": "CREDIT",
                  "amount": 150.00,
                  "currency": "USD",
                  "eventTimestamp": "2026-05-15T14:02:11Z",
                  "metadata": "mainframe-batch"
                }
                """;

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return validation error for invalid request")
    void shouldReturnValidationError() throws Exception {

        String request = """
                {
                  "eventId": "",
                  "accountId": "acct-123",
                  "type": "CREDIT",
                  "amount": -10,
                  "currency": "USD"
                }
                """;

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should calculate account balance correctly")
    void shouldReturnCorrectBalance() throws Exception {

        String creditEvent = """
                {
                  "eventId": "evt-001",
                  "accountId": "acct-123",
                  "type": "CREDIT",
                  "amount": 200,
                  "currency": "USD",
                  "eventTimestamp": "2026-05-15T14:02:11Z"
                }
                """;

        String debitEvent = """
                {
                  "eventId": "evt-002",
                  "accountId": "acct-123",
                  "type": "DEBIT",
                  "amount": 50,
                  "currency": "USD",
                  "eventTimestamp": "2026-05-15T15:02:11Z"
                }
                """;

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(creditEvent));

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(debitEvent));

        mockMvc.perform(get("/accounts/acct-123/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("150.00"));
    }

    @Test
    @DisplayName("Should fetch event by ID successfully")
    void shouldGetEventById() throws Exception {

        String request = """
            {
              "eventId": "evt-100",
              "accountId": "acct-100",
              "type": "CREDIT",
              "amount": 100,
              "currency": "USD",
              "eventTimestamp": "2026-05-15T14:02:11Z"
            }
            """;

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));

        Long eventId = repository.findAll().get(0).getId();

        mockMvc.perform(get("/events/" + eventId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundForInvalidEventId() throws Exception {

        mockMvc.perform(get("/events/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnEventsInChronologicalOrder() throws Exception {

        String event1 = """
            {
              "eventId": "evt-001",
              "accountId": "acct-123",
              "type": "CREDIT",
              "amount": 100,
              "currency": "USD",
              "eventTimestamp": "2026-05-15T15:02:11Z"
            }
            """;

        String event2 = """
            {
              "eventId": "evt-002",
              "accountId": "acct-123",
              "type": "DEBIT",
              "amount": 50,
              "currency": "USD",
              "eventTimestamp": "2026-05-15T14:02:11Z"
            }
            """;

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(event1));

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(event2));

        mockMvc.perform(get("/events?account=acct-123"))
                .andExpect(status().isOk());
    }

}
