package com.modsen.eventstore.service.impl;

import com.modsen.eventstore.dto.EventDto;
import com.modsen.eventstore.dto.EventWithIdDto;
import com.modsen.eventstore.exception.NotExistEntityException;
import com.modsen.eventstore.mapper.EventMapper;
import com.modsen.eventstore.model.Event;
import com.modsen.eventstore.repository.EventRepository;
import com.modsen.eventstore.service.EventService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Validated
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;


    @Override
    public EventWithIdDto create(@Valid EventDto dto) {
        log.info(String.format("Try to save event entity in data base by dto %s.", Objects.isNull(dto) ? null : dto.toString()));

        Assert.notNull(dto, "The dto for saving event entity cannot be null.");

        Event event = eventMapper.dtoToEntity(dto);
        return eventMapper.entityToWithIdDto(eventRepository.save(event));
    }

    @Override
    public EventWithIdDto read(Long id) {
        log.info(String.format("Try to find event entity with id = %d.", id));

        Assert.notNull(id, "Id to search for the entity event cannot be null.");

        Event event = eventRepository.findById(id).orElseThrow(() ->
                new NotExistEntityException(
                        String.format("Event entity with id = %d not exist in data base.", id)
                ));

        return eventMapper.entityToWithIdDto(event);
    }

    @Override
    public EventWithIdDto update(@Valid EventWithIdDto dto) {
        log.info(String.format("Try to update event entity in data base by dto %s.", Objects.isNull(dto) ? null : dto.toString()));

        Assert.notNull(dto, "The dto for updating event entity can't be null.");

        Event event = eventMapper.withIdDtoToEntity(dto);
        return eventMapper.entityToWithIdDto(eventRepository.update(event));
    }

    @Override
    public void delete(Long id) {
        log.info(String.format("Try to delete event with id = %d.", id));

        Assert.notNull(id, "The event entity being deleted can't have a null id.");

        eventRepository.delete(id);
    }

    @Override
    public List<EventWithIdDto> readAll() {
        log.info("Try to read all events.");
        List<Event> eventList = eventRepository.findAll();
        return eventMapper.entityListToWithIdDtoList(eventList);
    }

}