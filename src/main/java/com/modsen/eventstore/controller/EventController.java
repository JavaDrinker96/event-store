package com.modsen.eventstore.controller;

import com.modsen.eventstore.dto.criteria.PaginationCriteria;
import com.modsen.eventstore.dto.criteria.SortingDirection;
import com.modsen.eventstore.dto.criteria.event.EventCriteria;
import com.modsen.eventstore.dto.criteria.event.EventCriteriaField;
import com.modsen.eventstore.dto.criteria.event.EventFilterCriteria;
import com.modsen.eventstore.dto.criteria.event.EventSortingCriteria;
import com.modsen.eventstore.dto.event.EventRequest;
import com.modsen.eventstore.dto.event.EventResponse;
import com.modsen.eventstore.mapper.EventCriteriaMapper;
import com.modsen.eventstore.mapper.EventMapper;
import com.modsen.eventstore.model.Event;
import com.modsen.eventstore.service.EventService;
import com.modsen.eventstore.validator.GlobalValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Validated
@Api(tags = "Endpoints for events")
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final EventCriteriaMapper criteriaMapper;
    private final GlobalValidator validator;


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiOperation(value = "Register new event")
    public EventResponse registerEvent(@Valid @RequestBody EventRequest dto) {
        Event event = eventMapper.requestDtoToEntity(dto);
        return eventMapper.entityToResponseDto(eventService.create(event));
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Getting a specific event by id")
    public EventResponse getEvent(@PathVariable @NotNull @Min(1) Long id) {
        return eventMapper.entityToResponseDto(eventService.read(id));
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Changing information about an existing event")
    public EventResponse changeEvent(@Valid @RequestBody EventResponse dto) {
        Event event = eventMapper.responseDtoToEntity(dto);
        return eventMapper.entityToResponseDto(eventService.update(event));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Deleting an event by id")
    public void deleteEvent(@PathVariable @NotNull @Min(1) Long id) {
        eventService.delete(id);
    }

    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    public List<EventResponse> getAllEvents(@RequestParam(required = false) List<EventCriteriaField> filteredFields,
                                            @RequestParam(required = false) List<String> filteredValues,
                                            @RequestParam(required = false) List<EventCriteriaField> sortedFields,
                                            @RequestParam(required = false) List<SortingDirection> sortedDirections,
                                            @RequestParam(required = false) Integer page,
                                            @RequestParam(required = false) Integer size) {

        List<EventFilterCriteria> filterList = criteriaMapper.mapToListFilterCriteria(filteredFields, filteredValues);
        List<EventSortingCriteria> sortingList = criteriaMapper.mapToListSortingCriteria(sortedFields, sortedDirections);
        PaginationCriteria pagination = criteriaMapper.mapToPaginationCriteria(page, size);
        EventCriteria criteria = EventCriteria.of(sortingList, filterList, pagination);

        validator.validate(criteria);
        List<Event> events = Objects.isNull(criteria) ? eventService.readAll() : eventService.readAll(criteria);
        return eventMapper.entityListToResponseDtoList(events);
    }

}