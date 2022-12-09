package com.modsen.eventstore.service.impl;

import com.modsen.eventstore.dto.criteria.event.EventCriteria;
import com.modsen.eventstore.exception.BeforeTodayDateException;
import com.modsen.eventstore.exception.NotExistEntityException;
import com.modsen.eventstore.model.Event;
import com.modsen.eventstore.repository.EventRepository;
import com.modsen.eventstore.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;


    @Override
    public Event create(Event event) {
        log.info("Try to save entity {} into data base.", event);
        Assert.notNull(event, "The entity being saved cannot be null.");

        if (event.getDate().isBefore(LocalDate.now())) {
            throw new BeforeTodayDateException("The date must be present or future.");
        }

        return eventRepository.save(event);
    }

    @Override
    public Event read(Long id) {
        log.info("Try to find event entity with id = {}.", id);
        Assert.notNull(id, "Id to search for the entity event cannot be null.");
        return eventRepository.findById(id).orElseThrow(
                () -> new NotExistEntityException(String.format("Event entity with id = %d does not exist in the data base.", id))
        );
    }

    @Override
    public Event update(Event event) {
        log.info("Try to update the entity {}.", event);
        Assert.notNull(event, "The entity being updated cannot be null.");
        return eventRepository.update(event);
    }

    @Override
    public void delete(Long id) {
        log.info("Try to delete event with id = {}.", id);
        Assert.notNull(id, "The event entity being deleted can't have a null id.");
        eventRepository.delete(id);
    }

    @Override
    public List<Event> readAll() {
        log.info("Try to read all events.");
        return eventRepository.findAll();
    }

    @Override
    public List<Event> readAll(EventCriteria criteria) {
        log.info("Try to read all events by criteria {}.", criteria);
        Assert.notNull(criteria, "The criteria for finding values can't be null.");
        return eventRepository.findAll(criteria);
    }

}