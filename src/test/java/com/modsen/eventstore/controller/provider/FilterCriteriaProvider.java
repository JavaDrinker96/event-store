package com.modsen.eventstore.controller.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class FilterCriteriaProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("SUBJECT", "ab"),
                Arguments.of("SUBJECT", "a".repeat(151)),
                Arguments.of("DATE", "2000.01.01"),
                Arguments.of("PLANNER", "abcd"),
                Arguments.of("PLANNER", "a".repeat(151)),
                Arguments.of("TIME", "00:00:00")
        );
    }
}