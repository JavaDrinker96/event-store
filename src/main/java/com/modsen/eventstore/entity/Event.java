package com.modsen.eventstore.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "event")
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "events_id_seq_gen")
    @SequenceGenerator(name = "events_id_seq_gen", sequenceName = "events_id_seq", allocationSize = 10)
    private Long id;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "planner_full_name", nullable = false)
    private String plannerFullName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "venue", nullable = false)
    private String venue;

}