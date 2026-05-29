package com.assignment.eventledger.service;

import com.assignment.eventledger.dto.EventRequest;
import com.assignment.eventledger.entity.Event;
import com.assignment.eventledger.entity.EventType;
import com.assignment.eventledger.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository repository;

    public Event createEvent(EventRequest request) {

        Optional<Event> existingEvent =
                repository.findByEventId(request.getEventId());

        if (existingEvent.isPresent()) {
            return existingEvent.get();
        }

        Event event = Event.builder()
                .eventId(request.getEventId())
                .accountId(request.getAccountId())
                .type(request.getType())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .eventTimestamp(request.getEventTimestamp())
                .metadata(request.getMetadata())
                .build();

        return repository.save(event);
    }

    public Event getEvent(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Event not found"));
    }

    public List<Event> getEventsByAccount(String accountId) {
        return repository
                .findByAccountIdOrderByEventTimestampAsc(accountId);
    }

    public BigDecimal getBalance(String accountId) {

        List<Event> events =
                repository.findByAccountIdOrderByEventTimestampAsc(accountId);

        return events.stream()
                .map(event -> {
                    if (event.getType() == EventType.CREDIT) {
                        return event.getAmount();
                    } else {
                        return event.getAmount().negate();
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
