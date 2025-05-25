package com.demo.api.tests;

import com.demo.api.utilities.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contains tests related to deleting users via DELETE requests.
 * Assumes the user has been previously created during test run.
 */
public class UserDeleteTests extends BaseTest {
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
    public void shouldDeleteUserSuccessfully_whenUserExists() {
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
