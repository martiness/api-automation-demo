package com.demo.api.tests;

import com.demo.api.base.BaseTest;
import com.demo.api.utilities.UserApiHelper;
import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Contains tests related to retrieving user data via GET requests.
 * Covers listing users, fetching by ID, and negative cases (non-existing users).
 */
public class UserCreateTests extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(UserCreateTests.class);
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
    @Epic("User API")
    @Feature("Create User")
    @Story("As a tester, I want to create a user via POST")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("User Creation")
    @Description("Test aiming to create a unique new user by sending a POST request to the /api/users endpoint.")
    @Test
    public void shouldCreateNewUser_whenPostingValidData()
    {
        // Generate unique user name
        String uniqueName = "John_" + System.currentTimeMillis();

        // Prepare request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", uniqueName);
        requestBody.put("job", "QA Engineer");

        // Send POST request using helper
        Response response = UserApiHelper.createUser(withApiKey, requestBody);

        // Assert status code
        assertEquals(201, response.statusCode(), "Expected 201 Created");

        // Parse and assert response body
        JsonPath json = response.jsonPath();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(json.getString("name"))
                .as("Check name field")
                .isEqualTo(uniqueName);
        softly.assertThat(json.getString("job"))
                .as("Check job field")
                .isEqualTo("QA Engineer");
        softly.assertThat(json.getString("id"))
                .as("Check id presence")
                .isNotNull();
        softly.assertThat(json.getString("createdAt"))
                .as("Check creation timestamp")
                .isNotNull();
        softly.assertAll();

        // Log created user ID
        LOGGER.info("New created user ID: " + json.getString("id"));
    }
}
