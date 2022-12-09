package com.modsen.eventstore.service;

import com.modsen.eventstore.dto.criteria.PaginationCriteria;
import com.modsen.eventstore.dto.criteria.SortingDirection;
import com.modsen.eventstore.dto.criteria.event.EventCriteria;
import com.modsen.eventstore.dto.criteria.event.EventCriteriaField;
import com.modsen.eventstore.dto.criteria.event.EventFilterCriteria;
import com.modsen.eventstore.dto.criteria.event.EventSortingCriteria;
import com.modsen.eventstore.dto.event.EventResponse;
import com.modsen.eventstore.exception.NotExistEntityException;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    private EventService underTest;
    private AutoCloseable autoCloseable;

    private static final Long id = 1L;
    private static final String subject = "Subject";
    private static final String description = "Description";
    private static final String plannerFullName = "Full Name";
    private static final LocalDate date = LocalDate.of(2000, 1, 1);
    private static final LocalTime time = LocalTime.of(0, 0, 0);
    private static final String venue = "Venue";


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new EventServiceImpl(eventRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldSaveEvent_WhenDataIsCorrect() {
        //given
        LocalDate date = LocalDate.now();
        Event eventWithoutId = new Event(null, subject, description, plannerFullName, date, time, venue);
        Event expected = new Event(id, subject, description, plannerFullName, date, time, venue);

        when(eventRepository.save(eventWithoutId)).thenReturn(expected);

        //when
        Event result = underTest.create(eventWithoutId);

        //then
        verify(eventRepository).save(eventWithoutId);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void itShouldThrowException_WhenSavedDtoIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> underTest.create(null));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity being saved cannot be null.");
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
        Optional<Event> optionalEvent = Optional.of(createEvent(id));
        Event expected = createEvent(id);

        when(eventRepository.findById(id)).thenReturn(optionalEvent);

        //when
        Event result = underTest.read(id);

        //then
        verify(eventRepository).findById(id);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void itShouldThrowException_WhenReadingEventDoesNotExist() {
        //given
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        //when
        Throwable thrown = catchThrowable(() -> underTest.read(id));

        //then
        verify(eventRepository).findById(id);
        assertThat(thrown).isInstanceOf(NotExistEntityException.class)
                .hasMessageContaining(String.format("Event entity with id = %d does not exist in the data base.", id));
    }

    @Test
    void itShouldUpdateEvent_WhenDataIsCorrect() {
        //given
        Event expected = createEvent(id);

        when(eventRepository.update(expected)).thenReturn(expected);

        //when
        Event result = underTest.update(expected);

        //then
        verify(eventRepository).update(expected);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void itShouldThrowException_WhenUpdatedDtoIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> underTest.update(null));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity being updated cannot be null.");
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
        Event event1 = createEvent(1L);
        Event event2 = createEvent(2L);
        List<Event> expected = new ArrayList<>(List.of(event1, event2));

        when(eventRepository.findAll()).thenReturn(expected);

        //when
        List<Event> result = underTest.readAll();

        //then
        verify(eventRepository).findAll();
        assertThat(result).hasSize(2)
                .containsAll(expected);
    }

    @Test
    void itShouldNotReadEvents_WhenTheyAreNotExisting() {
        //given
        List<Event> eventList = new ArrayList<>();
        List<EventResponse> expected = new ArrayList<>();

        when(eventRepository.findAll()).thenReturn(eventList);

        //when
        List<Event> result = underTest.readAll();

        //then
        verify(eventRepository).findAll();
        assertThat(result).isEmpty();
    }

    @Test
    void itShouldThrowException_WhenTryReadEventsWithNullCriteria() {
        //when
        Throwable thrown = catchThrowable(() -> underTest.readAll(null));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The criteria for finding values can't be null");
    }

    @Test
    void itShouldReadAllEventsByCriteria_WhenDataIsCorrect() {
        //given
        Event event1 = new Event(1L, "Subject1", description, plannerFullName, date, time, venue);
        Event event2 = new Event(2L, "Subject2", description, plannerFullName, date, time, venue);
        List<Event> expected = List.of(event1, event2);

        EventFilterCriteria filterCriteria = new EventFilterCriteria(EventCriteriaField.SUBJECT, subject);
        EventSortingCriteria sortingCriteria = new EventSortingCriteria(EventCriteriaField.SUBJECT, SortingDirection.ASC);
        EventCriteria criteria = EventCriteria.builder()
                .filter(List.of(filterCriteria))
                .sort(List.of(sortingCriteria))
                .pagination(new PaginationCriteria(1, 1))
                .build();

        when(eventRepository.findAll(criteria)).thenReturn(expected);

        //when
        List<Event> result = underTest.readAll(criteria);

        //then
        verify(eventRepository).findAll(criteria);
        assertThat(result).hasSize(2)
                .containsExactly(event1, event2);
    }

    private Event createEvent(Long id) {
        return new Event(id, subject, description, plannerFullName, date, time, venue);
    }

}