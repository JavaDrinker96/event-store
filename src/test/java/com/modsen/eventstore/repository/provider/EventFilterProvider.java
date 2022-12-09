package com.modsen.eventstore.repository.provider;

import com.modsen.eventstore.dto.criteria.event.EventCriteriaField;
import com.modsen.eventstore.dto.criteria.event.EventFilterCriteria;
import com.modsen.eventstore.model.Event;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;

public class EventFilterProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(
                        new EventFilterCriteria(EventCriteriaField.SUBJECT, "Expected"),
                        new Event(1L, "Expected", "Description", "Full Name",
                                LocalDate.of(2000, 1, 1), LocalTime.of(0, 0, 0), "Venue")
                ),
                Arguments.of(
                        new EventFilterCriteria(EventCriteriaField.PLANNER, "Expected"),
                        new Event(2L, "Subject", "Description", "Expected",
                                LocalDate.of(2000, 1, 1), LocalTime.of(0, 0, 0), "Venue")
                ),
                Arguments.of(
                        new EventFilterCriteria(EventCriteriaField.DATE, "12.12.2012"),
                        new Event(3L, "Subject", "Description", "Full Name",
                                LocalDate.of(2012, 12, 12), LocalTime.of(0, 0, 0), "Venue")
                ),
                Arguments.of(
                        new EventFilterCriteria(EventCriteriaField.TIME, "01:01"),
                        new Event(4L, "Subject", "Description", "Full Name",
                                LocalDate.of(2000, 1, 1), LocalTime.of(1, 1, 0), "Venue")
                )
        );
    }
}