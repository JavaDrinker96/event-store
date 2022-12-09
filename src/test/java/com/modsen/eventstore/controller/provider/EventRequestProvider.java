package com.modsen.eventstore.controller.provider;

import com.modsen.eventstore.dto.event.EventRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class EventRequestProvider extends BaseEventProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                //subject
                Arguments.of(new EventRequest("   ", correctDescription, correctPlannerFullName, correctDate, correctTime, correctVenue)),
                Arguments.of(new EventRequest(null, correctDescription, correctPlannerFullName, correctDate, correctTime, correctVenue)),
                Arguments.of(new EventRequest("ab", correctDescription, correctPlannerFullName, correctDate, correctTime, correctVenue)),
                Arguments.of(new EventRequest("a".repeat(151), correctDescription, correctPlannerFullName, correctDate, correctTime, correctVenue)),

                //description
                Arguments.of(new EventRequest(correctSubject, "123456789", correctPlannerFullName, correctDate, correctTime, correctVenue)),
                Arguments.of(new EventRequest(correctSubject, "a".repeat(501), correctPlannerFullName, correctDate, correctTime, correctVenue)),

                //planner
                Arguments.of(new EventRequest(correctSubject, correctDescription, "     ", correctDate, correctTime, correctVenue)),
                Arguments.of(new EventRequest(correctSubject, correctDescription, null, correctDate, correctTime, correctVenue)),
                Arguments.of(new EventRequest(correctSubject, correctDescription, "abcd", correctDate, correctTime, correctVenue)),
                Arguments.of(new EventRequest(correctSubject, correctDescription, "a".repeat(151), correctDate, correctTime, correctVenue)),

                //date
                Arguments.of(new EventRequest(correctSubject, correctDescription, correctSubject, null, correctTime, correctVenue)),
                Arguments.of(new EventRequest(correctSubject, correctDescription, correctSubject, "2000.01.01", correctTime, correctVenue)),
                Arguments.of(new EventRequest(correctSubject, correctDescription, correctSubject, "01/01/2000", correctTime, correctVenue)),

                //time
                Arguments.of(new EventRequest(correctSubject, correctDescription, correctSubject, correctDate, null, correctVenue)),
                Arguments.of(new EventRequest(correctSubject, correctDescription, correctSubject, correctDate, "00:00:00", correctVenue)),

                //venue
                Arguments.of(new EventRequest(correctSubject, correctDescription, correctPlannerFullName, correctDate, correctTime, "   ")),
                Arguments.of(new EventRequest("   ", correctDescription, correctPlannerFullName, correctDate, correctTime, null)),
                Arguments.of(new EventRequest(correctSubject, correctDescription, correctPlannerFullName, correctDate, correctTime, "ab")),
                Arguments.of(new EventRequest(correctSubject, correctDescription, correctPlannerFullName, correctDate, correctTime, "a".repeat(151)))
        );
    }

}