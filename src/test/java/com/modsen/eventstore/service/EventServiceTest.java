package com.modsen.eventstore.service;

import com.modsen.eventstore.dto.EventDto;
import com.modsen.eventstore.dto.EventWithIdDto;
import com.modsen.eventstore.exception.NotExistEntityException;
import com.modsen.eventstore.mapper.EventMapper;
import com.modsen.eventstore.model.Event;
import com.modsen.eventstore.repository.EventRepository;
import com.modsen.eventstore.service.impl.EventServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    private EventService underTest;
    private AutoCloseable autoCloseable;

    private final Long id = 1L;
    private final String subject = "Subject";
    private final String description = "Description";
    private final String plannerFullName = "Full Name";
    private final String stringDate = "01.01.2000";
    private final LocalDate date = LocalDate.of(2000, 1, 1);
    private final String stringTime = "00:00";
    private final LocalTime time = LocalTime.of(0, 0, 0);
    private final String venue = "Venue";


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new EventServiceImpl(eventMapper, eventRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldSaveEvent_WhenDataIsCorrect() {
        //given
        EventDto dto = buildEventDto();
        Event event = buildEvent(true);
        Event eventWithoutId = buildEvent(false);
        EventWithIdDto expected = buildEventWithIdDto();

        when(eventMapper.dtoToEntity(dto)).thenReturn(eventWithoutId);
        when(eventRepository.save(eventWithoutId)).thenReturn(event);
        when(eventMapper.entityToWithIdDto(event)).thenReturn(expected);

        //when
        EventWithIdDto result = underTest.create(dto);

        //then
        assertThat(result).isEqualTo(expected);
        verify(eventRepository).save(eventWithoutId);
        verify(eventMapper).dtoToEntity(dto);
        verify(eventMapper).entityToWithIdDto(event);
    }

    @Test
    void itShouldThrowException_WhenSavedDtoIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> underTest.create(null));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The dto for saving event entity cannot be null.");
    }

    @Test
    void itShouldThrowException_WhenReadingIdIsNull() {

        //when
        Throwable thrown = catchThrowable(() -> underTest.read(null));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id to search for the entity event cannot be null.");
    }

    @Test
    void itShouldReadEvent_WhenDataIsCorrect() {
        //given
        Optional<Event> optionalEvent = Optional.of(buildEvent(true));
        Long id = optionalEvent.get().getId();
        EventWithIdDto expected = buildEventWithIdDto();

        when(eventRepository.findById(id)).thenReturn(optionalEvent);
        when(eventMapper.entityToWithIdDto(optionalEvent.get())).thenReturn(expected);

        //when
        EventWithIdDto result = underTest.read(id);

        //then
        assertThat(result).isEqualTo(expected);
        verify(eventRepository).findById(id);
        verify(eventMapper).entityToWithIdDto(optionalEvent.get());
    }

    @Test
    void itShouldThrowException_WhenReadingEventDoesNotExist() {
        //given
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        //when
        Throwable thrown = catchThrowable(() -> underTest.read(id));

        //then
        assertThat(thrown).isInstanceOf(NotExistEntityException.class)
                .hasMessageContaining(String.format("Event entity with id = %d not exist in data base.", id));
        verify(eventRepository).findById(id);
    }

    @Test
    void itShouldUpdateEvent_WhenDataIsCorrect() {
        //given
        EventWithIdDto expected = buildEventWithIdDto();
        Event event = buildEvent(true);

        when(eventMapper.withIdDtoToEntity(expected)).thenReturn(event);
        when(eventRepository.update(event)).thenReturn(event);
        when(eventMapper.entityToWithIdDto(event)).thenReturn(expected);

        //when
        EventWithIdDto result = underTest.update(expected);

        //then
        assertThat(result).isEqualTo(expected);
        verify(eventMapper).withIdDtoToEntity(expected);
        verify(eventRepository).update(event);
        verify(eventMapper).entityToWithIdDto(event);
    }

    @Test
    void itShouldThrowException_WhenUpdatedDtoIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> underTest.update(null));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The dto for updating event entity can't be null.");
    }

    @Test
    void itShouldDeleteEvent_WhenDataIsCorrect() {
        //when
        underTest.delete(id);

        //then
        verify(eventRepository).delete(id);
    }

    @Test
    void itShouldThrowException_WhenDeletedEventsIdIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> underTest.delete(null));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The event entity being deleted can't have a null id.");
    }

    @Test
    void itShouldReadAllEvents_WhenTheyAreExisting() {
        //given
        Event event1 = buildEvent(false);
        event1.setId(1L);
        Event event2 = buildEvent(false);
        event1.setId(2L);
        List<Event> eventList = new ArrayList<>(List.of(event1, event2));

        EventWithIdDto dto1 = buildEventWithIdDto();
        dto1.setId(1L);
        EventWithIdDto dto2 = buildEventWithIdDto();
        dto2.setId(2L);
        List<EventWithIdDto> expected = new ArrayList<>(List.of(dto1, dto2));

        when(eventRepository.findAll()).thenReturn(eventList);
        when(eventMapper.entityListToWithIdDtoList(eventList)).thenReturn(expected);

        //when
        List<EventWithIdDto> result = underTest.readAll();

        //then
        assertThat(result).hasSize(2)
                .containsAll(expected);
    }

    @Test
    void itShouldNotReadEvents_WhenTheyAreNotExisting(){
        //given
        List<Event> eventList = new ArrayList<>();
        List<EventWithIdDto> expected = new ArrayList<>();

        when(eventRepository.findAll()).thenReturn(eventList);
        when(eventMapper.entityListToWithIdDtoList(eventList)).thenReturn(expected);

        //when
        List<EventWithIdDto> result = underTest.readAll();

        //then
        assertThat(result).isEmpty();
    }

    private EventWithIdDto buildEventWithIdDto() {
        EventDto data = buildEventDto();
        return EventWithIdDto.builder()
                .id(id)
                .data(data)
                .build();
    }

    private EventDto buildEventDto() {
        return EventDto.builder()
                .subject(subject)
                .description(description)
                .plannerFullName(plannerFullName)
                .date(stringDate)
                .time(stringTime)
                .venue(venue)
                .build();
    }

    private Event buildEvent(boolean withId) {
        return Event.builder()
                .id(withId ? id : null)
                .subject(subject)
                .description(description)
                .date(date)
                .time(time)
                .plannerFullName(plannerFullName)
                .venue(venue)
                .build();
    }

}