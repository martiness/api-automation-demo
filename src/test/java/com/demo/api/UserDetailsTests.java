package com.demo.api;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDetailsTests extends BaseTest{

    /**
     * Test to verify that a specific user can be retrieved by ID and that their details are correct.
     *
     * FLOW:
     * 1. GET all users on page 1 to extract a valid user ID.
     * 2. Use the extracted ID to GET /api/users/{id}.
     * 3. Validate user response fields such as id, first name, last name, and email.
     *
     * SEND:
     *   - GET https://reqres.in/api/users?page=1
     *   - GET https://reqres.in/api/users/{userId}
     * HEADERS:
     *   - x-api-key: reqres-free-v1 (set globally in BaseTest)
     * EXPECT:
     *   - Status 200 OK
     *   - Valid and non-null user details in response
     */
    @Test
    public void getUserByID_withRequestSpec_shouldReturnUserDetails() {
        // Prepare request using global specification
        RequestSpecification request = RestAssured.given();

        // Step 1: Get first page of users and extract the first user ID
        Response listResponse = request
                .queryParam("page", 1)
                .when()
                .get("/api/users");

        assertEquals(200, listResponse.statusCode(), "Expected 200 OK");

        int userId = listResponse.jsonPath().getInt("data[0].id");

        // Step 2: Request that specific user by ID
        Response userResponse = request
                .when()
                .get("/api/users/" + userId);

        assertEquals(200, userResponse.statusCode(), "Expected 200 OK");

        // Step 3: Validate user details
        JsonPath jsonPath = userResponse.jsonPath();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(jsonPath.getInt("data.id")).isEqualTo(userId);
        softly.assertThat(jsonPath.getString("data.first_name")).isNotNull();
        softly.assertThat(jsonPath.getString("data.last_name")).isNotNull();
        softly.assertThat(jsonPath.getString("data.email")).contains("@reqres.in");
        softly.assertAll();
    }

    /**
     * Test to verify that requesting a non-existing user returns 404 Not Found.
     *
     * SEND:    GET https://reqres.in/api/users/999
     * HEADERS: x-api-key: reqres-free-v1 (set globally via BaseTest)
     * EXPECT:  Status 404 and empty JSON body "{}"
     */
    @Test
    public void getNonExistingUser_shouldReturn404() {
        int nonExistingUserId = 999;

        // Prepare request using global specification (includes base URI and headers)
        RequestSpecification request = RestAssured.given();

        // Send GET request for a user that does not exist
        Response response = request
                .when()
                .get("/api/users/" + nonExistingUserId);

        // Assert that response status is 404 Not Found
        assertEquals(404, response.statusCode(), "Expected 404 Not Found");

        // Assert that response body is an empty JSON object
        String responseBody = response.getBody().asString();
        System.out.println("Response body content: " + responseBody);
        assertEquals("{}", responseBody.trim(), "Expected empty JSON body");
    }
}
