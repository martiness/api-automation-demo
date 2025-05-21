package com.demo.api;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewUserTests extends BaseTest {

    /**
     * Test aiming to create a unique new user by sending a POST request to the /api/users endpoint.
     *
     * SEND:     POST https://reqres.in/api/users
     * HEADERS:  x-api-key: reqres-free-v1
     * BODY:     { "name": "John Unique", "job": "QA Engineer" }
     *
     * RESPONSE:
     *   Status: 201 Created
     *   Body:   Contains name, job, id, createdAt
     */

    @Test
    public void createUser_shouldReturn201AndUserDetails()
    {
        // Prepare payload (with generated unique name)
        String uniqueName = "John_" + System.currentTimeMillis();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", uniqueName);
        requestBody.put("job", "QA Engineer");

        // Send POST request
        Response response = RestAssured
                .given()
                .body(requestBody)
                .when()
                .post("/api/users");

        // Assert status code
        assertEquals(201, response.statusCode(), "Expected 201 Created");

        // Parse response and verify
        JsonPath json = response.jsonPath();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(json.getString("name")).isEqualTo(uniqueName);
        softly.assertThat(json.getString("job")).isEqualTo("QA Engineer");
        softly.assertThat(json.getString("id")).isNotNull();
        softly.assertThat(json.getString("createdAt")).isNotNull();
        softly.assertAll();

        // Print the created user info
        System.out.println("New user created: " + json.getString("id"));
    }
}
