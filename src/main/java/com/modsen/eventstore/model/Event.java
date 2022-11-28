package com.modsen.eventstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "description")
    private String description;

    @Column(name = "planner_full_name", nullable = false)
    private String plannerFullName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "venue", nullable = false)
    private String venue;

    @Override
    public Event clone() {
        Event event = null;
        try {
            event = (Event) super.clone();
        } catch (CloneNotSupportedException e) {
            event = new Event(
                    this.getId(), this.getSubject(), this.getDescription(), this.getPlannerFullName(),
                    this.getDate(), this.getTime(), this.getVenue()
            );
        }
        return event;
    }

}