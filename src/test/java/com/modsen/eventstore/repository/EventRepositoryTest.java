package com.modsen.eventstore.repository;

import com.modsen.eventstore.config.ContainersEnvironment;
import com.modsen.eventstore.exception.NotExistEntityException;
import com.modsen.eventstore.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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

    private Event event;

    @BeforeEach
    void setUp() {
        event = Event.builder()
                .subject("Subject")
                .description(null)
                .date(LocalDate.of(2000, 1, 1))
                .time(LocalTime.of(0, 0, 0))
                .plannerFullName("Full Name")
                .venue("Venue")
                .build();
    }

    @Test
    @DisplayName("Saving entity")
    void itShouldSaveEntity_WhenDataIsCorrect() {
        //when
        underTest.save(event);
        Optional<Event> result = underTest.read(event.getId());

        //then
        assertThat(event.getId()).isNotNull();
        assertThat(result).isPresent()
                .contains(event);
    }

    @Test
    @DisplayName("Saving null entity")
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
    @DisplayName("Saving entity with not null or not zero id")
    void itShouldThrowException_WhenSaveEntityWithNotNullOrNotZeroId() {
        //given
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
    @DisplayName("Reading entity by id")
    void itShouldRead_WhenEntityExists() {
        //given
        underTest.save(event);

        //when
        Optional<Event> result = underTest.read(event.getId());

        //then
        assertThat(result).isPresent()
                .contains(event);
    }

    @Test
    @DisplayName("Reading a non-existent entity")
    void itShouldNotRead_WhenEntityDoesNotExist() {
        //when
        Optional<Event> result = underTest.read(0L);

        //then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Reading entity with null id")
    void itShouldThrowException_WhenReadEntityIdIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> {
            underTest.read(null);
        });

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The id to search for an entity cannot be null.");
    }

    @Test
    @DisplayName("Updating entity")
    void itShouldUpdate_WhenDataIsCorrect() {
        //given
        underTest.save(event);
        Event expected = event.clone();
        expected.setSubject("New subject");

        //when
        underTest.update(expected);
        Optional<Event> result = underTest.read(event.getId());

        //then
        assertThat(result).isPresent()
                .contains(expected);
    }

    @Test
    @DisplayName("Updating null entity")
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
    @DisplayName("Updating entity with null id")
    void itShouldThrowException_WhenUpdatedEntityIdIsNull() {
        //given
        event.setId(null);

        //when
        Throwable thrown = catchThrowable(() -> {
            underTest.update(event);
        });

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The id of the entity being updated cannot be null.");
    }

    @Test
    @DisplayName("Updating a non-existent entity")
    void itShouldThrowException_WhenUpdatedEntityDoesNotExist() {
        //given
        underTest.save(event);
        event.setId(event.getId() + 1L);

        //when
        Throwable thrown = catchThrowable(() -> {
            underTest.update(event);
        });

        //then
        assertThat(thrown).isInstanceOf(NotExistEntityException.class)
                .hasMessageContaining("Entity of type Event with id = %d not exist in data base.", event.getId());
    }


    @Test
    @DisplayName("Deleting entity by id")
    void itShouldDelete_WhenDataIsCorrect() {
        //given
        underTest.save(event);

        //when
        underTest.delete(event.getId());
        Optional<Event> result = underTest.read(event.getId());

        //then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deleting a non-existent entity by id")
    void itShouldThrowException_WhenDeletedEntityDoesNotExist() {
        //when
        Throwable thrown = catchThrowable(() -> {
            underTest.delete(0L);
        });

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("attempt to create delete event with null entity");
    }

    @Test
    @DisplayName("Deleting entity by null id")
    void itShouldThrowException_WhenDeletedEntityIdIsNull() {
        //when
        Throwable thrown = catchThrowable(() -> {
            underTest.delete(null);
        });

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The id of the entity being deleted cannot be null.");
    }

    @Test
    @DisplayName("Reading all entities")
    void itShouldReadAllEntities_WhenTheyAreExist() {
        //given
        Event expected1 = underTest.save(event.clone());
        Event expected2 = underTest.save(event.clone());

        //when
        List<Event> result = underTest.readAll();

        //then
        assertThat(result).containsAll(List.of(expected1, expected2));
    }

    @Test
    @DisplayName("Reading non-existent entities")
    void itShouldNotReadEntities_WhenTheyAreNotExist() {
        //given
        List<Event> allEntities = underTest.readAll();
        allEntities.stream()
                .mapToLong(Event::getId)
                .forEach(id -> underTest.delete(id));

        //when
        List<Event> result = underTest.readAll();

        //then
        assertThat(result).isEmpty();
    }

}