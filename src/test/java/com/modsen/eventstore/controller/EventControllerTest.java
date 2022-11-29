package com.modsen.eventstore.controller;

import com.modsen.eventstore.BaseTest;
import com.modsen.eventstore.controller.provider.EventRequestProvider;
import com.modsen.eventstore.controller.provider.EventResponseProvider;
import com.modsen.eventstore.controller.provider.FilterCriteriaProvider;
import com.modsen.eventstore.dto.event.EventRequest;
import com.modsen.eventstore.dto.event.EventResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = "/scripts/delete_all_events.sql")
class EventControllerTest extends BaseTest {

    private static final String basePath = "/event";
    private static final String readPath = "/event/all";
    private static final String subject = "Subject";
    private static final String description = null;
    private static final String plannerFullName = "Full Name";
    private static final String date = "01.01.2222";
    private static final String time = "00:00";
    private static final String venue = "Venue";


    @Test
    void itShouldSaveEvent_WhenDataIsCorrect() throws Exception {
        //given
        EventRequest dto = createRequestDto();

        //when
        Response response = post(basePath, dto);

        //then
        EventResponse result = response.as(EventResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(201);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getSubject()).isEqualTo(subject);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getPlannerFullName()).isEqualTo(plannerFullName);
        assertThat(result.getDate()).isEqualTo(date);
        assertThat(result.getTime()).isEqualTo(time);
        assertThat(result.getVenue()).isEqualTo(venue);
    }

    @Test
    void itShouldThrowException_WhenDateIsBeforeThanToday() {
        //given
        String beforeDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        EventRequest dto = new EventRequest(subject, description, plannerFullName, beforeDate, time, venue);

        //when
        Response response = post(basePath, dto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @ParameterizedTest
    @ArgumentsSource(EventRequestProvider.class)
    void itShouldThrowException_WhenSavedDtoIsNotCorrect(EventRequest dto) {
        //when
        Response response = post(basePath, dto);

        //then
        ResponseBody body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    @Sql(scripts = "/scripts/insert_simple_event.sql")
    void itShouldUpdateEvent_WhenDataIsCorrect() {
        //given
        Long id = 1L;
        String subject = "New subject";
        String description = "New description";
        String plannerFullName = "New Full Name";
        String date = "02.01.2022";
        String time = "01:01";
        String venue = "New Venue";

        EventResponse dto = new EventResponse(id, subject, description, plannerFullName, date, time, venue);

        //when
        Response response = put(basePath, dto);

        //then
        EventResponse result = response.as(EventResponse.class);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void itShouldThrowException_WhenUpdatedDtoIdIsNull() {
        //given
        EventResponse dto = createResponseDto(null);

        //when
        Response response = put(basePath, dto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    void itShouldThrowException_WhenUpdatedDtoIdIsLessThan1() throws Exception {
        //given
        EventResponse dto = createResponseDto(0L);

        //when
        Response response = put(basePath, dto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @ParameterizedTest
    @ArgumentsSource(EventResponseProvider.class)
    void itShouldThrowException_WhenUpdatedDtoDataIsNotCorrect(EventResponse dto, String expectedMessage) throws Exception {
        //when
        Response response = put(basePath, dto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    @Sql(scripts = "/scripts/insert_simple_event.sql")
    void itShouldReadEvent_WhenItExist() throws Exception {
        //given
        Long id = 1L;
        EventResponse expected = createResponseDto(id);

        //when
        Response response = get(basePath, id);

        //then
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.as(EventResponse.class)).isEqualTo(expected);
    }

    @Test
    void itShouldThrowException_WhenReadEventDoesNotExist() throws Exception {
        //given
        Long id = 100L;

        //when
        Response response = get(basePath, id);

        //then
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    @Sql(scripts = "/scripts/insert_simple_event.sql")
    void itShouldDeleteEvent_WhenDataIsCorrect() {
        //given
        Long id = 1L;

        //when
        Response response = delete(basePath, id);

        //then
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    void itShouldThrowException_WhenDeletedEventDoesNotExist() {
        //given
        Long id = 1L;

        //when
        Response response = delete(basePath, id);

        //then
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    @Sql(scripts = {
            "/scripts/delete_all_events.sql",
            "/scripts/insert_two_simple_events.sql"
    })
    void itShouldReadAllEvents_WhenCriteriaIsNullAndTheyAreExisting() throws Exception {
        //given
        EventResponse dto1 = createResponseDto(1L);
        EventResponse dto2 = createResponseDto(2L);
        String url = String.format("%s/all", basePath);

        //when
        Response response = get(url, Map.of());

        //then
        List<EventResponse> result = response.jsonPath().getList("$", EventResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(result).containsExactly(dto1, dto2);
    }

    @Test
    @Sql(scripts = "/scripts/insert_events_for_testing_filtering.sql")
    void itShouldReadFilteredEvent_WhenFilterIsCorrectAndDataAreExisting() throws Exception {
        //given
        Long id = 1L;
        EventResponse expected = EventResponse.builder()
                .id(id)
                .subject("Expected")
                .description("Description")
                .plannerFullName("Full Name")
                .date("01.01.2000")
                .time("00:00")
                .venue("Venue")
                .build();

        String url = String.format("%s/all", basePath);
        Map<String, String> params = Map.ofEntries(
                entry("filteredFields", "SUBJECT"),
                entry("filteredValues", "Expected")
        );

        //when
        Response response = get(url, params);

        //then
        assertThat(response.getStatusCode()).

                isEqualTo(200);

        assertThat(response.jsonPath().

                getList("$", EventResponse.class)).

                containsExactly(expected);

    }

    @Test
    void itShouldThrowException_WhenFieldInSortingCriteriaIsNull() throws Exception {
        //given
        Map<String, String> params = Map.of("sortedDirections", "ASC");

        //when
        Response response = get(readPath, params);

        //then
        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(response.asString()).contains("A sortable field cannot exist without a sorting direction and vice versa.");
    }

    @Test
    void itShouldThrowException_WhenDirectionInSortingCriteriaIsNull() throws Exception {
        //given
        Map<String, String> params = Map.of("sortedFields", "SUBJECT");

        //when
        Response response = get(readPath, params);

        //then
        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(response.asString()).contains("A sortable field cannot exist without a sorting direction and vice versa.");
    }

    @ParameterizedTest
    @ArgumentsSource(FilterCriteriaProvider.class)
    void itShouldThrowException_WhenFilterCriteriaIsNotValid(String field, String value) throws Exception {
        //given
        Map<String, String> params = Map.ofEntries(
                entry("filteredFields", field),
                entry("filteredValues", value)
        );

        //when
        Response response = get(readPath, params);

        //then
        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(response.asString()).contains("The value of all event filter criteria must match the regular expression of the selected field name");
    }

    private EventRequest createRequestDto() {
        return new EventRequest(subject, description, plannerFullName, date, time, venue);
    }

    private EventResponse createResponseDto(Long id) {
        return new EventResponse(id, subject, description, plannerFullName, date, time, venue);
    }

}