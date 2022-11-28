package com.modsen.eventstore.repository.impl;

import com.modsen.eventstore.exception.NotExistEntityException;
import com.modsen.eventstore.model.Event;
import com.modsen.eventstore.repository.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class EventRepositoryImpl implements EventRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Event save(Event event) {
        log.info(String.format("Try to save entity %s in data base.", Objects.isNull(event) ? null : event.toString()));

        Assert.notNull(event, "The entity being saved cannot be null.");

        if (Objects.nonNull(event.getId()) && event.getId() != 0L) {
            throw new IllegalArgumentException("The id of the entity being saved must be null or zero.");
        }

        entityManager.persist(event);
        return event;
    }

    @Override
    public Optional<Event> read(Long id) {
        log.info(String.format("Try to find event entity with id = %d.", id));

        Assert.notNull(id, "The id to search for an entity cannot be null.");

        return Optional.ofNullable(entityManager.find(Event.class, id));
    }

    @Override
    public Event update(Event event) {
        log.info(String.format("Try to update entity %s in data base.", Objects.isNull(event) ? null : event.toString()));

        Assert.notNull(event, "The entity being updated cannot be null.");
        Assert.notNull(event.getId(), "The id of the entity being updated cannot be null.");

        if (Objects.isNull(entityManager.find(Event.class, event.getId()))) {
            throw new NotExistEntityException(
                    String.format("Entity of type Event with id = %d not exist in data base.", event.getId())
            );
        }

        return entityManager.merge(event);
    }

    @Override
    public void delete(Long id) {

        log.info(String.format("Try to delete event with id = %d.", id));

        Assert.notNull(id, "The id of the entity being deleted cannot be null.");

        Event event = entityManager.find(Event.class, id);
        entityManager.remove(event);
    }

    @Override
    public List<Event> readAll() {

        log.info("Try read all events.");

        return entityManager.createQuery("SELECT e FROM Event e", Event.class).getResultList();
    }

}