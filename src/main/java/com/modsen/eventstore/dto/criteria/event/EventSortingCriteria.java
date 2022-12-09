package com.modsen.eventstore.dto.criteria.event;

import com.modsen.eventstore.dto.criteria.SortingDirection;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class EventSortingCriteria {

    private EventCriteriaField field;

    private SortingDirection direction;

}