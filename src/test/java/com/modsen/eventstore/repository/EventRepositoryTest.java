package com.modsen.eventstore.repository;

import com.modsen.eventstore.BaseTest;
import com.modsen.eventstore.dto.criteria.PaginationCriteria;
import com.modsen.eventstore.dto.criteria.event.EventCriteria;
import com.modsen.eventstore.dto.criteria.event.EventFilterCriteria;
import com.modsen.eventstore.dto.criteria.event.EventSortingCriteria;
import com.modsen.eventstore.exception.NotExistEntityException;
import com.modsen.eventstore.model.Event;
import com.modsen.eventstore.repository.provider.EventFilterProvider;
import com.modsen.eventstore.repository.provider.EventSortingProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@Sql(scripts = "/scripts/delete_all_events.sql")
class EventRepositoryTest extends BaseTest {

    @Autowired
    private EventRepository underTest;


    @Test
    void itShouldSaveEntity_WhenDataIsCorrect() {
        //given
        Event given = buildEvent(null);

        //when
        Event result = underTest.save(given);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(given);
    }

    @Test
    void itShouldThrowException_WhenSaveNullEntity() {
        //when
        Throwable thrown = catchThrowable(() -> underTest.save(null));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity being saved cannot be null.");
    }

    @Test
    void itShouldThrowException_WhenSavedEntityWithNotNullOrNotZeroId() {
        //given
        Event event = buildEvent(1L);

        //when
        Throwable thrown = catchThrowable(() -> underTest.save(event));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The id of the entity being saved must be null or zero.");
    }

    @Test
    @Sql(scripts = "/scripts/insert_simple_event.sql")
    void itShouldRead_WhenEntityExists() {
        //given
        Event expected = buildEvent(1L);

        //when
        Optional<Event> result = underTest.findById(expected.getId());

        //then
        assertThat(result).isPresent()
                .contains(expected);
    }

    @Test
    void itShouldNotRead_WhenEntityDoesNotExist() {
        //when
        Optional<Event> result = underTest.findById(1L);

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void itShouldThrowException_WhenReadEntityIdIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> underTest.findById(null));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The id to search for an entity cannot be null.");
    }

    @Test
    @Sql(scripts = "/scripts/insert_simple_event.sql")
    void itShouldUpdate_WhenDataIsCorrect() {
        //given
        Event expected = buildEvent(1L);
        expected.setSubject("New subject");

        //when
        Event result = underTest.update(expected);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void itShouldThrowException_WhenEntityIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> underTest.update(null));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity being updated cannot be null.");
    }

    @Test
    void itShouldThrowException_WhenUpdatedEntityIdIsNull() {
        //given
        Event event = buildEvent(null);

        //when
        Throwable thrown = catchThrowable(() -> underTest.update(event));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The id of the entity being updated cannot be null.");
    }

    @Test
    void itShouldThrowException_WhenUpdatedEntityDoesNotExist() {
        //given
        Event event = buildEvent(1L);

        //when
        Throwable thrown = catchThrowable(() -> underTest.update(event));

        //then
        assertThat(thrown).isInstanceOf(NotExistEntityException.class)
                .hasMessageContaining("Entity of type Event with id = %d not exist in data base.", event.getId());
    }


    @Test
    void itShouldThrowException_WhenDeletedEntityDoesNotExist() {
        //given
        Long id = 1L;

        //when
        Throwable thrown = catchThrowable(() -> underTest.delete(id));

        //then
        assertThat(thrown).isInstanceOf(NotExistEntityException.class)
                .hasMessageContaining(String.format("The entity being deleted with id = %s does not exist in the database", id));
    }

    @Test
    void itShouldThrowException_WhenDeletedEntityIdIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> underTest.delete(null));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The id of the entity being deleted cannot be null.");
    }

    @Test
    @Sql(scripts = {
            "/scripts/delete_all_events.sql",
            "/scripts/insert_two_simple_events.sql"
    })
    void itShouldReadAllEntities_WhenTheyAreExisting() {
        //given
        Event expected1 = buildEvent(1L);
        Event expected2 = buildEvent(2L);

        //when
        List<Event> result = underTest.findAll();

        //then
        assertThat(result).hasSize(2)
                .containsAll(List.of(expected1, expected2));
    }

    @Test
    void itShouldNotReadEntities_WhenTheyAreNotExisting() {
        //when
        List<Event> result = underTest.findAll();

        //then
        assertThat(result).isEmpty();
    }

    @Test
    @Sql(scripts ={
            "/scripts/delete_all_events.sql",
            "/scripts/insert_two_simple_events.sql"
    })
    @DisplayName("Test reading all event entities when filtering, sorting and pagination are not specified")
    void itShouldReadAllEntities_WhenEventFilterCriteriaAndSortingCriteriaAndPaginationAreNull() {
        //given
        Event expected1 = buildEvent(1L);
        Event expected2 = buildEvent(2L);
        EventCriteria criteria = EventCriteria.builder()
                .filter(null)
                .sort(null)
                .pagination(null)
                .build();

        //when
        List<Event> result = underTest.findAll(criteria);

        //then
        assertThat(result).hasSize(2)
                .containsAll(List.of(expected1, expected2));
    }

    @Test
    void itShouldThrowException_WhenCriteriaIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> underTest.findAll(null));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The criteria for finding values can't be null");
    }

    @ParameterizedTest
    @Sql(scripts = {
            "/scripts/delete_all_events.sql",
            "/scripts/insert_events_for_testing_filtering.sql"
    })
    @ArgumentsSource(EventFilterProvider.class)
    void itShouldReadFilteredEvents_WhenTheyAreExisting(EventFilterCriteria filterCriteria, Event expected) {
        //given
        EventCriteria criteria = EventCriteria.builder().filter(List.of(filterCriteria)).build();

        //when
        List<Event> result = underTest.findAll(criteria);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(expected);
    }

    @ParameterizedTest
    @Sql(scripts = {
            "/scripts/delete_all_events.sql",
            "/scripts/insert_events_for_testing_sorting.sql"
    })
    @ArgumentsSource(EventSortingProvider.class)
    void iTShouldReadSortedEvents_WhenTheyAreExisting(EventSortingCriteria sortingCriteria, Event[] expected) {
        //given
        EventCriteria criteria = EventCriteria.of(List.of(sortingCriteria), null, null);

        //when
        List<Event> result = underTest.findAll(criteria);

        //then
        assertThat(result).hasSize(4)
                .containsExactly(expected);
    }

    @Test
    @Sql(scripts = "/scripts/insert_two_simple_events.sql")
    void itShouldReadPageOfEvents_WhenTheyAreExisting() {
        //given
        Event expected = buildEvent(2L);
        PaginationCriteria paginationCriteria = PaginationCriteria.builder().page(2).size(1).build();
        EventCriteria eventCriteria = EventCriteria.builder().pagination(paginationCriteria).build();

        //when
        List<Event> result = underTest.findAll(eventCriteria);

        //then
        assertThat(result).hasSize(1)
                .contains(expected);
    }

    @Test
    void itShouldThrowException_WhenNumberOfPageLessThan1() {
        //given
        PaginationCriteria paginationCriteria = PaginationCriteria.builder().page(0).size(1).build();
        EventCriteria eventCriteria = EventCriteria.builder().pagination(paginationCriteria).build();

        //when
        Throwable thrown = catchThrowable(() -> underTest.findAll(eventCriteria));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("first-result value cannot be negative");
    }

    @Test
    void itShouldThrowException_WhenPageSizeLessThan0() {
        //given
        PaginationCriteria paginationCriteria = PaginationCriteria.builder().page(1).size(-1).build();
        EventCriteria eventCriteria = EventCriteria.builder().pagination(paginationCriteria).build();

        //when
        Throwable thrown = catchThrowable(() -> underTest.findAll(eventCriteria));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("max-results cannot be negative");
    }

    private Event buildEvent(Long id) {
        return Event.builder()
                .id(id)
                .subject("Subject")
                .description(null)
                .date(LocalDate.of(2222, 1, 1))
                .time(LocalTime.of(0, 0, 0))
                .plannerFullName("Full Name")
                .venue("Venue")
                .build();
    }

}