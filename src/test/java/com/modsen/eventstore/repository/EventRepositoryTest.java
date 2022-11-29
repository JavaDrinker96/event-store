package com.modsen.eventstore.repository;

import com.modsen.eventstore.config.ContainersEnvironment;
import com.modsen.eventstore.exception.NotExistEntityException;
import com.modsen.eventstore.model.Event;
import com.modsen.eventstore.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ActiveProfiles("test")
@SpringBootTest
class EventRepositoryTest extends ContainersEnvironment {

    @Autowired
    private EventRepository underTest;

    @Autowired
    private EventService eventService;

    @Test
    void itShouldSaveEntity_WhenDataIsCorrect() {
        //given
        Event given = buildEvent();

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
        Throwable thrown = catchThrowable(() -> {
            underTest.save(null);
        });

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity being saved cannot be null.");
    }

    @Test
    void itShouldThrowException_WhenSavedEntityWithNotNullOrNotZeroId() {
        //given
        Event event = buildEvent();
        event.setId(1L);

        //when
        Throwable thrown = catchThrowable(() -> {
            underTest.save(event);
        });

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The id of the entity being saved must be null or zero.");
    }

    @Test
    void itShouldRead_WhenEntityExists() {
        //given
        Event event = buildEvent();
        Event given = underTest.save(event);

        //when
        Optional<Event> result = underTest.findById(given.getId());

        //then
        assertThat(result).isPresent()
                .contains(given);
    }

    @Test
    @Sql(scripts = "/scripts/deleteAllFromEventsTable.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void itShouldNotRead_WhenEntityDoesNotExist() {
        //when
        Optional<Event> result = underTest.findById(1L);

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void itShouldThrowException_WhenReadEntityIdIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> {
            underTest.findById(null);
        });

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The id to search for an entity cannot be null.");
    }

    @Test
    void itShouldUpdate_WhenDataIsCorrect() {
        //given
        Event saved = underTest.save(buildEvent());
        Event expected = buildEvent();
        expected.setId(saved.getId());
        expected.setSubject("New subject");

        //when
        Event result = underTest.update(expected);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void itShouldThrowException_WhenEntityIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> {
            underTest.update(null);
        });

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity being updated cannot be null.");
    }

    @Test
    void itShouldThrowException_WhenUpdatedEntityIdIsNull() {
        //given
        Event event = buildEvent();

        //when
        Throwable thrown = catchThrowable(() -> {
            underTest.update(event);
        });

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The id of the entity being updated cannot be null.");
    }

    @Test
    @Sql(scripts = "/scripts/deleteAllFromEventsTable.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void itShouldThrowException_WhenUpdatedEntityDoesNotExist() {
        //given
        Event event = buildEvent();
        event.setId(1L);

        //when
        Throwable thrown = catchThrowable(() -> {
            underTest.update(event);
        });

        //then
        assertThat(thrown).isInstanceOf(NotExistEntityException.class)
                .hasMessageContaining("Entity of type Event with id = %d not exist in data base.", event.getId());
    }

    @Test
    void itShouldDelete_WhenDataIsCorrect() {
        //given
        Event given = underTest.save(buildEvent());

        //when
        underTest.delete(given.getId());
    }

    @Test
    @Sql(scripts = "/scripts/deleteAllFromEventsTable.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void itShouldThrowException_WhenDeletedEntityDoesNotExist() {
        //when
        Throwable thrown = catchThrowable(() -> {
            underTest.delete(1L);
        });

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("attempt to create delete event with null entity");
    }

    @Test
    void itShouldThrowException_WhenDeletedEntityIdIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> {
            underTest.delete(null);
        });

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The id of the entity being deleted cannot be null.");
    }

    @Test
    @Sql(scripts = "/scripts/deleteAllFromEventsTable.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void itShouldReadAllEntities_WhenTheyAreExist() {
        //given
        Event expected1 = underTest.save(buildEvent());
        Event expected2 = underTest.save(buildEvent());

        //when
        List<Event> result = underTest.findAll();

        //then
        assertThat(result).hasSize(2)
                .containsAll(List.of(expected1, expected2));
    }

    @Test
    @Sql(scripts = "/scripts/deleteAllFromEventsTable.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void itShouldNotReadEntities_WhenTheyAreNotExist() {
        //when
        List<Event> result = underTest.findAll();

        //then
        assertThat(result).isEmpty();
    }

    private Event buildEvent(){
        return Event.builder()
                .subject("Subject")
                .description("Description")
                .date(LocalDate.of(2000, 1, 1))
                .time(LocalTime.of(0, 0, 0))
                .plannerFullName("Full Name")
                .venue("Venue")
                .build();
    }

}