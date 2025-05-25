package com.demo.api.tests;

import com.demo.api.untilities.BaseTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserOperationsTests extends BaseTest {

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
                .spec(withApiKey)
                .body(requestBody)
                .when()
                .post("/api/users");

        // Assert successful user creation
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
        System.out.println("New created user ID: " + json.getString("id"));
    }

    /**
     * Test aiming to delete a newly created user using DELETE request to /api/users/{id}.
     *
     * FLOW:
     *  1. Create a new user via POST /api/users
     *  2. Extract the generated user ID
     *  3. Delete the user using DELETE /api/users/{id}
     *  4. Assert that the response status is 204 (No Content)
     */
    @Test
    public void deleteUser_shouldReturn204() {
        // Create a user to delete
        String uniqueName = "ToBeDeleted_" + System.currentTimeMillis();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", uniqueName);
        requestBody.put("job", "To Be Deleted");

        //Create response
        Response createResponse = RestAssured
                .given()
                .spec(withApiKey)
                .body(requestBody)
                .when()
                .post("/api/users");

        // Assert successful user creation
        assertEquals(201, createResponse.statusCode(), "Expected 201 Created");

        // Print the created user info
        String userId = createResponse.jsonPath().getString("id");
        System.out.println("Created for deletion user ID: " + userId);

        // DELETE the created user
        Response deleteResponse = RestAssured
                .given()
                .spec(withApiKey)
                .when()
                .delete("/api/users/" + userId);

        // Assert DELETE response status
        assertEquals(204, deleteResponse.statusCode(), "Expected 204 No Content after deletion");

        // Assert body is empty
        assertTrue(deleteResponse.getBody().asString().isEmpty(), "Expected empty body after deletion");
    }
}
