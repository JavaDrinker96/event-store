package com.modsen.eventstore.mapper;

import com.modsen.eventstore.dto.event.EventRequest;
import com.modsen.eventstore.dto.event.EventResponse;
import com.modsen.eventstore.mapper.provider.EventRequestToEventProvider;
import com.modsen.eventstore.mapper.provider.EventToEventResponseProvider;
import com.modsen.eventstore.mapper.provider.EventResponseToEventProvider;
import com.modsen.eventstore.model.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EventMapperTest {

    private final EventMapper underTest = new EventMapperImpl();

    @ParameterizedTest
    @ArgumentsSource(EventRequestToEventProvider.class)
    void itShouldMapDtoToEntity(EventRequest dto, Event expected) {
        //when
        Event result = underTest.requestDtoToEntity(dto);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ArgumentsSource(EventResponseToEventProvider.class)
    void itShouldMapWithIdDtoToEntity(EventResponse dto, Event expected) {
        //when
        Event result = underTest.responseDtoToEntity(dto);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ArgumentsSource(EventToEventResponseProvider.class)
    void itShouldMapEventToEventWithIdDto(Event event, EventResponse expected) {
        //when
        EventResponse result = underTest.entityToResponseDto(event);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void itShouldReturnNull_DuringMapEntityListToWithIdDtoList_WhenDtoListIsNull() {
        //when
        List<EventResponse> result = underTest.entityListToResponseDtoList(null);

        //then
        assertThat(result).isNull();
    }

    @Test
    void itShouldReturnEmptyList_DuringMapEntityListToWithIdDtoList_WhenDtoListIsEmpty() {
        //when
        List<EventResponse> result = underTest.entityListToResponseDtoList(new ArrayList<>());

        //then
        assertThat(result).isEmpty();
    }

}