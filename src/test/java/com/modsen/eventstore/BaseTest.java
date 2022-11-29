package com.modsen.eventstore;

import com.modsen.eventstore.config.ContainersEnvironment;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public abstract class BaseTest extends ContainersEnvironment {

    @BeforeAll
    protected static void beforeAll() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }

    protected Response post(String url, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(body)
                .when()
                .post(url);
    }

    protected Response put(String url, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(body)
                .when()
                .put(url);
    }

    protected Response get(String url, Map<String, String> params) {
        return given()
                .contentType(ContentType.JSON)
                .params(params)
                .when()
                .get(url);
    }

    protected Response get(String url, Long id) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(String.format("%s/%d", url, id));
    }

    protected Response delete(String url, Long id) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete(String.format("%s/%d", url, id));
    }

}