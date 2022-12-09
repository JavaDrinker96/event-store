package com.modsen.eventstore.controller.provider;

public abstract class BaseEventProvider {

    protected String correctSubject = "Subject";
    protected String correctDescription = "Description";
    protected String correctPlannerFullName = "Full Name";
    protected String correctDate = "01.01.2222";
    protected String correctTime = "01:01";
    protected String correctVenue = "Venue";

    protected String subjectExceptionMessage = "Subject must be between 3 to 150 characters and be not blank";
    protected String descriptionExceptionMessage = "Description must be between 10 to 150 characters";
    protected String plannerExceptionMessage = "Full name of planner must be between 5 to 150 characters and be not blank";
    protected String dateExceptionMessage = "The date must match the template 'dd.mm.yyyy' and be not null";
    protected String timeExceptionMessage = "The time must match the template 'hh:mm' and be not null";
    protected String venueExceptionMessage = "Venue must be between 5 to 150 characters and be not blank";

}
