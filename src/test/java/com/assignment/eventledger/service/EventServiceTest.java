package com.assignment.eventledger.service;

import com.assignment.eventledger.dto.EventRequest;
import com.assignment.eventledger.entity.Event;
import com.assignment.eventledger.entity.EventType;
import com.assignment.eventledger.repository.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @Mock
    private EventRepository repository;

    @InjectMocks
    private EventService service;

    @Test
    @DisplayName("Should create new event successfully")
    void shouldCreateEventSuccessfully() {

        EventRequest request = new EventRequest();
        request.setEventId("evt-001");
        request.setAccountId("acct-123");
        request.setType(EventType.CREDIT);
        request.setAmount(BigDecimal.valueOf(100));
        request.setCurrency("USD");
        request.setEventTimestamp(Instant.now());

        when(repository.findByEventId("evt-001"))
                .thenReturn(Optional.empty());

        Event savedEvent = Event.builder()
                .id(1L)
                .eventId("evt-001")
                .accountId("acct-123")
                .type(EventType.CREDIT)
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .eventTimestamp(request.getEventTimestamp())
                .build();

        when(repository.save(any(Event.class)))
                .thenReturn(savedEvent);

        Event result = service.createEvent(request);

        assertNotNull(result);
        assertEquals("evt-001", result.getEventId());

        verify(repository, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("Should return existing event for duplicate eventId")
    void shouldHandleDuplicateEvent() {

        Event existingEvent = Event.builder()
                .id(1L)
                .eventId("evt-001")
                .build();

        when(repository.findByEventId("evt-001"))
                .thenReturn(Optional.of(existingEvent));

        EventRequest request = new EventRequest();
        request.setEventId("evt-001");

        Event result = service.createEvent(request);

        assertEquals("evt-001", result.getEventId());

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should calculate account balance correctly")
    void shouldCalculateBalanceCorrectly() {

        Event credit = Event.builder()
                .type(EventType.CREDIT)
                .amount(BigDecimal.valueOf(200))
                .build();

        Event debit = Event.builder()
                .type(EventType.DEBIT)
                .amount(BigDecimal.valueOf(50))
                .build();

        when(repository.findByAccountIdOrderByEventTimestampAsc("acct-123"))
                .thenReturn(List.of(credit, debit));

        BigDecimal balance = service.getBalance("acct-123");

        assertEquals(BigDecimal.valueOf(150), balance);
    }

    @Test
    @DisplayName("Should throw exception when event not found")
    void shouldThrowExceptionWhenEventNotFound() {

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.getEvent(999L)
        );

        assertEquals("Event not found", exception.getMessage());
    }
}
