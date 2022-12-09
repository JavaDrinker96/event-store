package com.modsen.eventstore.service;

import com.modsen.eventstore.dto.criteria.event.EventCriteria;
import com.modsen.eventstore.model.Event;

import java.util.List;

public interface EventService {

    Event create(Event event);

    Event read(Long id);

    Event update(Event event);

    void delete(Long id);

    List<Event> readAll();

    List<Event> readAll(EventCriteria criteria);

}