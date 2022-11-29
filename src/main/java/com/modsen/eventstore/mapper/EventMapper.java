package com.modsen.eventstore.mapper;

import com.modsen.eventstore.dto.event.EventRequest;
import com.modsen.eventstore.dto.event.EventResponse;
import com.modsen.eventstore.model.Event;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface EventMapper {

    String DATE_FORMAT = "dd.MM.yyyy";
    String TIME_FORMAT = "HH:mm";
    String TIME_REGEX_PATTERN = "^\\d{2}:\\d{2}$";

    @Mapping(target = "date", source = "date", dateFormat = DATE_FORMAT)
    @Mapping(target = "time", source = "time", qualifiedByName = "stringToTime")
    Event requestDtoToEntity(EventRequest dto);

    @Mapping(target = "date", source = "date", dateFormat = DATE_FORMAT)
    @Mapping(target = "time", source = "time", qualifiedByName = "stringToTime")
    Event responseDtoToEntity(EventResponse dto);

    @Mapping(target = "date", source = "date", dateFormat = DATE_FORMAT)
    @Mapping(target = "time", source = "time", qualifiedByName = "timeToString")
    @Named("entityToWithIdDto")
    EventResponse entityToResponseDto(Event entity);

    @IterableMapping(qualifiedByName = "entityToWithIdDto")
    List<EventResponse> entityListToResponseDtoList(List<Event> entity);

    @Named("timeToString")
    default String timeToString(LocalTime time) {
        return Objects.nonNull(time) ? time.format(DateTimeFormatter.ofPattern(TIME_FORMAT)) : null;
    }

    @Named("stringToTime")
    default LocalTime stringToTime(String string) {
        return (!Objects.isNull(string) && string.matches(TIME_REGEX_PATTERN))
                ? LocalTime.parse(string, DateTimeFormatter.ofPattern(TIME_FORMAT))
                : null;
    }

}