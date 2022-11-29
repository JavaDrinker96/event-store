package com.modsen.eventstore.mapper.provider;

import com.modsen.eventstore.dto.event.EventResponse;
import com.modsen.eventstore.model.Event;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;

public class EventToEventResponseProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(
                        new Event(null, null, null, null, null, null, null),
                        new EventResponse(null, null, null, null, null, null, null)
                ),
                Arguments.of(
                        new Event(1L, "Subject", "Description", "Full Name", LocalDate.of(2000, 1, 1), LocalTime.of(0, 0, 0), "Venue"),
                        new EventResponse(1L, "Subject", "Description", "Full Name", "01.01.2000", "00:00", "Venue")
                )
        );
    }
}
