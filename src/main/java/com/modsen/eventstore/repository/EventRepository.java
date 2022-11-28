package com.modsen.eventstore.repository;

import com.modsen.eventstore.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    Event save(Event event);

    Optional<Event> read(Long id);

    Event update(Event event);

    void delete(Long id);

    List<Event> readAll();

}