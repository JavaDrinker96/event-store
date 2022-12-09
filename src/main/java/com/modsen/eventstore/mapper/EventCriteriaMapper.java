package com.modsen.eventstore.mapper;

import com.modsen.eventstore.dto.criteria.PaginationCriteria;
import com.modsen.eventstore.dto.criteria.SortingDirection;
import com.modsen.eventstore.dto.criteria.event.EventCriteriaField;
import com.modsen.eventstore.dto.criteria.event.EventFilterCriteria;
import com.modsen.eventstore.dto.criteria.event.EventSortingCriteria;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface EventCriteriaMapper {

    default List<EventFilterCriteria> mapToListFilterCriteria(List<EventCriteriaField> fields, List<String> values) {
        List<EventFilterCriteria> filterCriteriaList = new ArrayList<>();

        if (Objects.isNull(fields) && Objects.isNull(values)) {
            return null;
        }

        if (Objects.isNull(fields) || Objects.isNull(values)) {
            throw new IllegalArgumentException("The filtered field cannot exist without the direction of the value and vice versa.");
        }

        if (fields.size() != values.size()) {
            throw new IllegalArgumentException("The number of fields to filter and their values must match.");
        }

        for (int i = 0; i < fields.size(); i++) {
            filterCriteriaList.add(new EventFilterCriteria(fields.get(0), values.get(0)));
        }

        return filterCriteriaList;
    }

    default List<EventSortingCriteria> mapToListSortingCriteria(List<EventCriteriaField> fields, List<SortingDirection> directions) {
        List<EventSortingCriteria> sortingCriteriaList = new ArrayList<>();

        if (Objects.isNull(fields) && Objects.isNull(directions)) {
            return null;
        }

        if (Objects.isNull(fields) || Objects.isNull(directions)) {
            throw new IllegalArgumentException("A sortable field cannot exist without a sorting direction and vice versa.");
        }

        if (fields.size() != directions.size()) {
            throw new IllegalArgumentException("The number of fields to sorting and their directions must match.");
        }

        for (int i = 0; i < fields.size(); i++) {
            sortingCriteriaList.add(new EventSortingCriteria(fields.get(0), directions.get(0)));
        }

        return sortingCriteriaList;
    }

    default PaginationCriteria mapToPaginationCriteria(Integer page, Integer size) {
        return Objects.isNull(page) && Objects.isNull(size) ? null : new PaginationCriteria(page, size);
    }

}
