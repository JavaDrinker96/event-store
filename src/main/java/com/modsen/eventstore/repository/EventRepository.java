package com.modsen.eventstore.repository;

import com.modsen.eventstore.dto.criteria.event.EventCriteria;
import com.modsen.eventstore.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    Event save(Event event);

    Optional<Event> findById(Long id);

    Event update(Event event);

    void delete(Long id);

    List<Event> findAll();

    List<Event> findAll(EventCriteria criteria);

}