package com.modsen.eventstore.repository.provider;

import com.modsen.eventstore.dto.criteria.SortingDirection;
import com.modsen.eventstore.dto.criteria.event.EventCriteriaField;
import com.modsen.eventstore.dto.criteria.event.EventSortingCriteria;
import com.modsen.eventstore.model.Event;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;

public class EventSortingProvider implements ArgumentsProvider {

    private static final Event event1 = new Event(1L, "Subject1", "Description", "Full Name1",
            LocalDate.of(2000, 1, 1), LocalTime.of(0, 1, 0), "Venue");
    private static final Event event2 = new Event(2L, "Subject4", "Description", "Full Name2",
            LocalDate.of(2000, 1, 3), LocalTime.of(0, 3, 0), "Venue");
    private static final Event event3 = new Event(3L, "Subject2", "Description", "Full Name4",
            LocalDate.of(2000, 1, 2), LocalTime.of(0, 4, 0), "Venue");
    private static final Event event4 = new Event(4L, "Subject3", "Description", "Full Name3",
            LocalDate.of(2000, 1, 4), LocalTime.of(0, 2, 0), "Venue");

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(
                        new EventSortingCriteria(EventCriteriaField.SUBJECT, SortingDirection.ASC),
                        new Event[]{event1, event3, event4, event2}
                ),
                Arguments.of(
                        new EventSortingCriteria(EventCriteriaField.SUBJECT, SortingDirection.DESC),
                        new Event[]{event2, event4, event3, event1}
                ),
                Arguments.of(
                        new EventSortingCriteria(EventCriteriaField.PLANNER, SortingDirection.ASC),
                        new Event[]{event1, event2, event4, event3}
                ),
                Arguments.of(
                        new EventSortingCriteria(EventCriteriaField.PLANNER, SortingDirection.DESC),
                        new Event[]{event3, event4, event2, event1}
                ),
                Arguments.of(
                        new EventSortingCriteria(EventCriteriaField.DATE, SortingDirection.ASC),
                        new Event[]{event1, event3, event2, event4}
                ),
                Arguments.of(
                        new EventSortingCriteria(EventCriteriaField.DATE, SortingDirection.DESC),
                        new Event[]{event4, event2, event3, event1}
                ),
                Arguments.of(
                        new EventSortingCriteria(EventCriteriaField.TIME, SortingDirection.ASC),
                        new Event[]{event1, event4, event2, event3}
                ),
                Arguments.of(
                        new EventSortingCriteria(EventCriteriaField.TIME, SortingDirection.DESC),
                        new Event[]{event3, event2, event4, event1}
                )
        );
    }
}
