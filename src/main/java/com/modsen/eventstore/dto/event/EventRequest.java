package com.modsen.eventstore.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
public class EventRequest {

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