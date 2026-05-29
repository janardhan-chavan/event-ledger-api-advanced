package com.assignment.eventledger.repository;

import com.assignment.eventledger.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventRepository extends JpaRepository<Event, Long>{
    Optional<Event> findByEventId(String eventId);

    List<Event> findByAccountIdOrderByEventTimestampAsc(String accountId);

    Page<Event> findByAccountIdOrderByEventTimestampAsc(
            String accountId,
            Pageable pageable);
}
