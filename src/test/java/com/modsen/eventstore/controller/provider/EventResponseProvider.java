package com.modsen.eventstore.controller.provider;

import com.modsen.eventstore.dto.event.EventResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class EventResponseProvider extends BaseEventProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                //subject
                Arguments.of(
                        new EventResponse(1L, "   ", correctDescription, correctPlannerFullName, correctDate, correctTime, correctVenue),
                        subjectExceptionMessage
                ),
                Arguments.of(
                        new EventResponse(1L, null, correctDescription, correctPlannerFullName, correctDate, correctTime, correctVenue),
                        subjectExceptionMessage
                ),
                Arguments.of(
                        new EventResponse(1L, "ab", correctDescription, correctPlannerFullName, correctDate, correctTime, correctVenue),
                        subjectExceptionMessage
                ),
                Arguments.of(
                        new EventResponse(1L, "a".repeat(151), correctDescription, correctPlannerFullName, correctDate, correctTime, correctVenue),
                        subjectExceptionMessage
                ),

                //description
                Arguments.of(
                        new EventResponse(1L, correctSubject, "123456789", correctPlannerFullName, correctDate, correctTime, correctVenue),
                        descriptionExceptionMessage
                ),
                Arguments.of(
                        new EventResponse(1L, correctSubject, "a".repeat(501), correctPlannerFullName, correctDate, correctTime, correctVenue),
                        descriptionExceptionMessage
                ),

                //planner
                Arguments.of(
                        new EventResponse(1L, correctSubject, correctDescription, "     ", correctDate, correctTime, correctVenue),
                        plannerExceptionMessage
                ),
                Arguments.of(
                        new EventResponse(1L, correctSubject, correctDescription, null, correctDate, correctTime, correctVenue),
                        plannerExceptionMessage
                ),
                Arguments.of(
                        new EventResponse(1L, correctSubject, correctDescription, "abcd", correctDate, correctTime, correctVenue),
                        plannerExceptionMessage
                ),
                Arguments.of(
                        new EventResponse(1L, correctSubject, correctDescription, "a".repeat(151), correctDate, correctTime, correctVenue),
                        plannerExceptionMessage
                ),

                //date
                Arguments.of(
                        new EventResponse(1L, correctSubject, correctDescription, correctSubject, null, correctTime, correctVenue),
                        dateExceptionMessage
                ),
                Arguments.of(
                        new EventResponse(1L, correctSubject, correctDescription, correctSubject, "2000.01.01", correctTime, correctVenue),
                        dateExceptionMessage
                ),
                Arguments.of(
                        new EventResponse(1L, correctSubject, correctDescription, correctSubject, "01/01/2000", correctTime, correctVenue),
                        dateExceptionMessage
                ),

                //time
                Arguments.of(
                        new EventResponse(1L, correctSubject, correctDescription, correctSubject, correctDate, null, correctVenue),
                        timeExceptionMessage
                ),
                Arguments.of(
                        new EventResponse(1L, correctSubject, correctDescription, correctSubject, correctDate, "00:00:00", correctVenue),
                        timeExceptionMessage
                ),

                //venue
                Arguments.of(
                        new EventResponse(1L, correctSubject, correctDescription, correctPlannerFullName, correctDate, correctTime, "   "),
                        venueExceptionMessage
                ),
                Arguments.of(
                        new EventResponse(1L, "   ", correctDescription, correctPlannerFullName, correctDate, correctTime, null),
                        venueExceptionMessage
                ),
                Arguments.of(
                        new EventResponse(1L, correctSubject, correctDescription, correctPlannerFullName, correctDate, correctTime, "ab"),
                        venueExceptionMessage
                ),
                Arguments.of(
                        new EventResponse(1L, correctSubject, correctDescription, correctPlannerFullName, correctDate, correctTime, "a".repeat(151)),
                        venueExceptionMessage
                )
        );
    }

}