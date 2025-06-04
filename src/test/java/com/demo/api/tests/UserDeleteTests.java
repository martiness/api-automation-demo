package com.demo.api.tests;

import com.demo.api.base.BaseTest;
import com.demo.api.utilities.UserApiHelper;

import io.qameta.allure.*;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contains tests related to deleting users via DELETE requests.
 * Assumes the user has been previously created during test run.
 */
public class UserDeleteTests extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(UserDeleteTests.class);
    /**
     * Test aiming to delete a newly created user using DELETE request to /api/users/{id}.
     * FLOW:
     *  1. Create a new user via POST /api/users
     *  2. Extract the generated user ID
     *  3. Delete the user using DELETE /api/users/{id}
     *  4. Assert that the response status is 204 (No Content)
     */
    @Epic("User API")
    @Feature("Delete User")
    @Story("As a tester, I want to Delete the user using DELETE T")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("User Deletion")
    @Description("Test aiming to delete a newly created user using DELETE request to /api/users/{id}.")
    @Test
    public void shouldDeleteUserSuccessfully_whenUserExists() {
        // Generate unique user name
        String uniqueName = "ToBeDeleted_" + System.currentTimeMillis();

        // Prepare request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", uniqueName);
        requestBody.put("job", "To Be Deleted");

        // Create user via helper
        Response createResponse = UserApiHelper.createUser(withApiKey, requestBody);
        assertEquals(201, createResponse.statusCode(), "Expected 201 Created");

        // Extract user ID
        String userId = createResponse.jsonPath().getString("id");
        LOGGER.info("Created user ID for deletion: " + userId);

        // Delete user via helper
        Response deleteResponse = UserApiHelper.deleteUserById(withApiKey, userId);

        // Assert DELETE response
        assertEquals(204, deleteResponse.statusCode(), "Expected 204 No Content after deletion");

        // Additional Assert response body is empty
        assertTrue(deleteResponse.getBody().asString().isEmpty(), "Expected empty body after deletion");
    }
}
