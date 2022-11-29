package com.modsen.eventstore.mapper;

import com.modsen.eventstore.dto.EventDto;
import com.modsen.eventstore.dto.EventWithIdDto;
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
    Event dtoToEntity(EventDto dto);

    @Mapping(target = "subject", source = "data.subject")
    @Mapping(target = "description", source = "data.description")
    @Mapping(target = "plannerFullName", source = "data.plannerFullName")
    @Mapping(target = "date", source = "data.date", dateFormat = DATE_FORMAT)
    @Mapping(target = "time", source = "data.time", qualifiedByName = "stringToTime")
    @Mapping(target = "venue", source = "data.venue")
    Event withIdDtoToEntity(EventWithIdDto dto);

    @Mapping(target = "data.subject", source = "subject")
    @Mapping(target = "data.description", source = "description")
    @Mapping(target = "data.plannerFullName", source = "plannerFullName")
    @Mapping(target = "data.date", source = "date", dateFormat = DATE_FORMAT)
    @Mapping(target = "data.time", source = "time", qualifiedByName = "timeToString")
    @Mapping(target = "data.venue", source = "venue")
    @Named("entityToWithIdDto")
    EventWithIdDto entityToWithIdDto(Event entity);

    @IterableMapping(qualifiedByName = "entityToWithIdDto")
    List<EventWithIdDto> entityListToWithIdDtoList(List<Event> entity);

    @Named("timeToString")
    default String timeToString(LocalTime time) {
        if (Objects.isNull(time)) {
            return null;
        }
        return time.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    @Named("stringToTime")
    default LocalTime stringToTime(String string) {
        if (Objects.isNull(string) || !string.matches(TIME_REGEX_PATTERN)) {
            return null;
        }
        return LocalTime.parse(string, DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

}