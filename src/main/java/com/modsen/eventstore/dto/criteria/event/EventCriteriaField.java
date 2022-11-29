package com.modsen.eventstore.dto.criteria.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventCriteriaField {

    SUBJECT("subject"),
    PLANNER("plannerFullName"),
    DATE("date"),
    TIME("time");

    private final String name;

}