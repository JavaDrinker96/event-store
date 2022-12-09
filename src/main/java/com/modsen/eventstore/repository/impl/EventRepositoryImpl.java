package com.modsen.eventstore.repository.impl;

import com.modsen.eventstore.dto.criteria.SortingDirection;
import com.modsen.eventstore.dto.criteria.event.EventCriteria;
import com.modsen.eventstore.dto.criteria.event.EventCriteriaField;
import com.modsen.eventstore.dto.criteria.event.EventFilterCriteria;
import com.modsen.eventstore.dto.criteria.event.EventSortingCriteria;
import com.modsen.eventstore.exception.NotExistEntityException;
import com.modsen.eventstore.model.Event;
import com.modsen.eventstore.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
        log.info("Try to save entity {} in data base.", event);
        Assert.notNull(event, "The entity being saved cannot be null.");

        if (Objects.nonNull(event.getId()) && !event.getId().equals(0L)) {
            throw new IllegalArgumentException("The id of the entity being saved must be null or zero.");
        }

        entityManager.persist(event);
        return event;
    }

    @Override
    public Optional<Event> findById(Long id) {
        log.info("Try to find event entity with id = {}.", id);
        Assert.notNull(id, "The id to search for an entity cannot be null.");
        return Optional.ofNullable(entityManager.find(Event.class, id));
    }

    @Override
    public Event update(Event event) {
        log.info("Try to update entity {} in data base.", event);
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
        log.info("Try to delete event with id = {}.", id);
        Assert.notNull(id, "The id of the entity being deleted cannot be null.");

        Event event = Optional.ofNullable(entityManager.find(Event.class, id)).orElseThrow(() ->
                new NotExistEntityException(String.format("The entity being deleted with id = %s does not exist in the database", id))
        );

        entityManager.remove(event);
    }

    @Override
    public List<Event> findAll() {
        log.info("Try read all events.");
        CriteriaQuery<Event> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);
        criteriaQuery.select(root);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Event> findAll(EventCriteria criteria) {
        log.info("Try to find event by criteria {}", criteria);

        Assert.notNull(criteria, "The criteria for finding values can't be null");

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = builder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);
        criteriaQuery.select(root);

        if (Objects.nonNull(criteria.getFilter())) {
            Predicate[] predicates = getFilterPredicateArray(criteria.getFilter(), builder, root);
            criteriaQuery.where(builder.and(predicates));
        }

        if (Objects.nonNull(criteria.getSort())) {
            Order[] orders = getSortingOrderArray(criteria.getSort(), builder, root);
            criteriaQuery.orderBy(orders);
        }

        if (Objects.nonNull(criteria.getPagination())) {
            return entityManager.createQuery(criteriaQuery)
                    .setFirstResult(criteria.getPagination().getPage() - 1)
                    .setMaxResults(criteria.getPagination().getSize())
                    .getResultList();
        }

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private Predicate[] getFilterPredicateArray(List<EventFilterCriteria> filterCriteriaList, CriteriaBuilder builder, Root<Event> root) {
        return filterCriteriaList.stream()
                .map(criteria -> buildFilterPredicate(criteria, builder, root))
                .filter(Objects::nonNull)
                .toArray(Predicate[]::new);
    }

    private Predicate buildFilterPredicate(EventFilterCriteria criteria, CriteriaBuilder builder, Root<Event> root) {
        String fieldName = criteria.getField().getName();
        String fieldValue = criteria.getValue();

        if (criteria.getField() == EventCriteriaField.SUBJECT ||
                criteria.getField() == EventCriteriaField.PLANNER) {
            return builder.equal(root.get(fieldName), fieldValue);
        }
        if (criteria.getField() == EventCriteriaField.DATE) {
            String dateFormat = "dd.MM.yyyy";
            LocalDate date = LocalDate.parse(fieldValue, DateTimeFormatter.ofPattern(dateFormat));
            return builder.equal(root.get(fieldName), date);
        }
        if (criteria.getField() == EventCriteriaField.TIME) {
            String timeFormat = "HH:mm";
            LocalTime time = LocalTime.parse(fieldValue, DateTimeFormatter.ofPattern(timeFormat));
            return builder.equal(root.get(fieldName), time);
        }

        return null;
    }

    private Order[] getSortingOrderArray(List<EventSortingCriteria> sortingCriteriaList, CriteriaBuilder builder, Root<Event> root) {
        return sortingCriteriaList.stream()
                .map(criteria -> buildSortingOrder(criteria, builder, root))
                .filter(Objects::nonNull)
                .toArray(Order[]::new);
    }

    private Order buildSortingOrder(EventSortingCriteria criteria, CriteriaBuilder builder, Root<Event> root) {
        if (criteria.getDirection() == SortingDirection.ASC) {
            return builder.asc(root.get(criteria.getField().getName()));
        }
        if (criteria.getDirection() == SortingDirection.DESC) {
            return builder.desc(root.get(criteria.getField().getName()));
        }

        return null;
    }

}