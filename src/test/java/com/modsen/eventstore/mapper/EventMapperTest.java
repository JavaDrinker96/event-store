package com.modsen.eventstore.mapper;

import com.modsen.eventstore.dto.EventDto;
import com.modsen.eventstore.dto.EventWithIdDto;
import com.modsen.eventstore.model.Event;
import com.modsen.eventstore.provider.EventDtoToEventProvider;
import com.modsen.eventstore.provider.EventToEventWithIdDtoProvider;
import com.modsen.eventstore.provider.EventWithIdDtoToEventProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EventMapperTest {

    private EventMapper underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new EventMapperImpl();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @ParameterizedTest(name = "Test mapping EventDto to Event: [{index}] - {arguments}")
    @ArgumentsSource(EventDtoToEventProvider.class)
    void itShouldMapDtoToEntity(EventDto dto, Event expected) {
        //when
        Event result = underTest.dtoToEntity(dto);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest(name = "Test mapping EventWithIdDto to Event: [{index}] - {arguments}")
    @ArgumentsSource(EventWithIdDtoToEventProvider.class)
    void itShouldMapWithIdDtoToEntity(EventWithIdDto dto, Event expected) {
        //when
        Event result = underTest.withIdDtoToEntity(dto);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest(name = "Test mapping Event to EventWithIdDto: [{index}] - {arguments}")
    @ArgumentsSource(EventToEventWithIdDtoProvider.class)
    void itShouldMapEventToEventWithIdDto(Event event, EventWithIdDto expected) {
        //when
        EventWithIdDto result = underTest.entityToWithIdDto(event);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void itShouldReturnNull_DuringMapEntityListToWithIdDtoList_WhenDtoListIsNull() {
        //when
        List<EventWithIdDto> result = underTest.entityListToWithIdDtoList(null);

        //then
        assertThat(result).isNull();
    }

    @Test
    void itShouldReturnEmptyList_DuringMapEntityListToWithIdDtoList_WhenDtoListIsEmpty() {
        //when
        List<EventWithIdDto> result = underTest.entityListToWithIdDtoList(new ArrayList<>());

        //then
        assertThat(result).isEmpty();
    }

}