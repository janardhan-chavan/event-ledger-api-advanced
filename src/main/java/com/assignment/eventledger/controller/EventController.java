package com.assignment.eventledger.controller;

import com.assignment.eventledger.dto.EventRequest;
import com.assignment.eventledger.entity.Event;
import com.assignment.eventledger.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class EventController {
    private final EventService service;

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(
            @Valid @RequestBody EventRequest request) {

        boolean alreadyExists =
                service.getEventsByAccount(request.getAccountId())
                        .stream()
                        .anyMatch(event ->
                                event.getEventId()
                                        .equals(request.getEventId()));

        Event savedEvent = service.createEvent(request);

        if (alreadyExists) {
            return ResponseEntity.ok(savedEvent);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedEvent);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEvent(id));
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEventsByAccount(
            @RequestParam String account) {

        return ResponseEntity.ok(
                service.getEventsByAccount(account));
    }

    @GetMapping("/accounts/{accountId}/balance")
    public ResponseEntity<BigDecimal> getBalance(
            @PathVariable String accountId) {

        return ResponseEntity.ok(
                service.getBalance(accountId));
    }

}
