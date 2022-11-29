package com.modsen.eventstore.service;

import com.modsen.eventstore.dto.EventDto;
import com.modsen.eventstore.dto.EventWithIdDto;

import java.util.List;

public interface EventService {

    EventWithIdDto create(EventDto dto);

    EventWithIdDto read(Long id);

    EventWithIdDto update(EventWithIdDto dto);

    void delete(Long id);

    List<EventWithIdDto> readAll();

}