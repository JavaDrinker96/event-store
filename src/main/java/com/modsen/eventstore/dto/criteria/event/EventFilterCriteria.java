package com.modsen.eventstore.dto.criteria.event;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class EventFilterCriteria {

    private EventCriteriaField field;

    private String value;

}
