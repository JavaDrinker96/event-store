package com.modsen.eventstore.dto.criteria.event;

import com.modsen.eventstore.dto.criteria.PaginationCriteria;
import com.modsen.eventstore.validator.filter.EventFilterList;
import com.modsen.eventstore.validator.pagination.Pagination;
import com.modsen.eventstore.validator.sorting.EventSortingList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class EventCriteria {

    @EventSortingList
    private List<EventSortingCriteria> sort;

    @EventFilterList
    private List<EventFilterCriteria> filter;

    @Pagination
    private PaginationCriteria pagination;

}