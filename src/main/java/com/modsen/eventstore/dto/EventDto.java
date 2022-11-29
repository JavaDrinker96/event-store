package com.modsen.eventstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class EventDto {


    @NotBlank(message = "Subject must be between 3 to 150 not white spaces characters")
    @Size(min = 3, max = 150)
    private String subject;

    @Size(min = 10, max = 500)
    private String description;

    @NotBlank(message = "Full name of planner must be between 5 to 150 not white spaces characters")
    @Size(min = 5,max = 150)
    private String plannerFullName;

    @NotNull(message = "The date must match the template 'dd.mm.yyyy'")
    @Pattern(regexp = "^\\d{2}.\\d{2}.\\d{4}$")
    private String date;

    @NotNull(message = "The time must match the template 'hh:mm'")
    @Pattern(regexp = "^\\d{2}:\\d{2}$")
    private String time;

    @NotBlank(message = "Venue must be between 5 to 150 not white spaces characters")
    @Size(min = 3,max = 130)
    private String venue;

}