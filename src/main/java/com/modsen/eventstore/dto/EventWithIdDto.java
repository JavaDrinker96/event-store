package com.modsen.eventstore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class EventWithIdDto {

    @NotNull(message = "Id must be more zen 0")
    @Positive
    private Long id;

    @NotNull
    private EventDto data;

}