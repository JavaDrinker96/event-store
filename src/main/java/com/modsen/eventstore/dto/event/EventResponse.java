package com.modsen.eventstore.dto.event;

import lombok.*;

import javax.validation.constraints.*;


@Data
@Builder
@AllArgsConstructor
public class EventResponse {

    @NotNull(message = "Id must be not null")
    @Positive(message = "Id must be more than 0")
    private Long id;

    @NotBlank(message = "Subject must be between 3 to 150 characters and be not blank")
    @Size(message = "Subject must be between 3 to 150 characters and be not blank", min = 3, max = 150)
    private String subject;

    @Size(message = "Description must be between 10 to 150 characters", min = 10, max = 500)
    private String description;

    @NotBlank(message = "Full name of planner must be between 5 to 150 characters and be not blank")
    @Size(message = "Full name of planner must be between 5 to 150 characters and be not blank", min = 5, max = 150)
    private String plannerFullName;

    @NotNull(message = "The date must match the template 'dd.mm.yyyy' and be not null")
    @Pattern(message = "The date must match the template 'dd.mm.yyyy' and be not null", regexp = "^\\d{2}\\.\\d{2}\\.\\d{4}$")
    private String date;

    @NotNull(message = "The time must match the template 'hh:mm' and be not null")
    @Pattern(message = "The time must match the template 'hh:mm' and be not null", regexp = "^\\d{2}:\\d{2}$")
    private String time;

    @NotBlank(message = "Venue must be between 5 to 150 characters and be not blank")
    @Size(message = "Venue must be between 5 to 150 characters and be not blank", min = 3, max = 130)
    private String venue;

}