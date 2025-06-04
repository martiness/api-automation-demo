package com.demo.api.tests;

import com.demo.api.base.BaseTest;
import com.demo.api.utilities.UserApiHelper;

import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Contains tests for retrieving user data via GET requests.
 * Covers pagination, user details, and sorting logic.
 */
public class UserReadTests extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(UserReadTests.class);

    /**
     * SEND:     GET https://reqres.in/api/users?page=2
     * HEADERS:  x-api-key: reqres-free-v1
     * RESPONSE:
     *    Status Code: 200 OK
     *    Body: JSON containing list of users on page 2
     *
     * Purpose:
     *    - Validate that the `/api/users` endpoint returns a successful (200 OK) response
     *      when requesting users on page 2.
     *    - Print the response body for manual inspection (exploratory/debugging use).
     */
    @Epic("User API")
    @Feature("Read User")
    @Story("As a tester, I want to send GET request and receive users list")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Users List")
    @Description("Validate that the `/api/users` endpoint returns a successful (200 OK) response")
    @Test
    public void shouldReturnUserList_whenRequestingPage2() {
        // Send GET request to the users endpoint
        Response response = UserApiHelper.listUsers(withApiKey, 2);

        // Verify that response status code is 200 OK
        assertEquals(200, response.getStatusCode(), "Expected 200 OK");
    }

    /**
     * SEND:     GET https://reqres.in/api/users?page=1
     * HEADERS:  x-api-key: reqres-free-v1
     * RESPONSE:
     *    Status Code: 200 OK
     *    Body: JSON with 6 user entries on page 1
     *
     * Purpose:
     *    - Verify that requesting users on page 1 returns a 200 OK status.
     *    - Assert that the first user in the response has expected values for
     *      first name, last name, email, and avatar.
     */
    @Test
    public void shouldMatchExpectedUserData_onPageOne() {
        // Send GET request to the users endpoint
        Response response = UserApiHelper.listUsers(withApiKey, 1);

        // Verify that response status code is 200 OK
        assertEquals(200, response.getStatusCode(), "Expected 200 OK");

        // Extract user list from response JSON
        List<Map<String, Object>> users = response.jsonPath().getList("data");
        assertEquals(6, users.size(), "Expected 6 Users");

        // Extract and validate first user details
        Map<String, Object> firstUser = users.get(0);

        // Soft assertions for user fields
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(firstUser.get("first_name"))
                .as("First Name")
                .isEqualTo("George");
        softly.assertThat(firstUser.get("last_name"))
                .as("Last Name")
                .isEqualTo("Bluth");
        softly.assertThat(firstUser.get("email"))
                .as("Email")
                .isEqualTo("george.bluth@reqres.in");
        softly.assertThat(firstUser.get("avatar"))
                .as("Avatar URL")
                .isEqualTo("https://reqres.in/img/faces/1-image.jpg");
        softly.assertAll();
    }

    /**
     * SEND:     GET https://reqres.in/api/users?page=2
     * HEADERS:  x-api-key: reqres-free-v1
     * RESPONSE:
     *    Status Code: 200 OK
     *    Body: JSON with 6 user entries on page 2
     *
     * Purpose:
     *    - Verify that requesting users on page 2 returns a 200 OK status.
     *    - Assert that the first user on the page has expected values:
     *      first name, last name, email, and avatar.
     */
    @Test
    public void shouldMatchExpectedUserData_onPageTwo() {
        // Send GET request to fetch users on page 2
        Response response = UserApiHelper.listUsers(withApiKey, 2);

        // Validate the response status code
        assertEquals(200, response.getStatusCode(), "Expected 200 OK");

        // Extract the list of users from the JSON response
        List<Map<String, Object>> users = response.jsonPath().getList("data");
        assertEquals(6, users.size(), "Expected 6 Users");

        // Access the first user in the list
        Map<String, Object> firstUser = users.get(0);

        // Use soft assertions for clarity
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(firstUser.get("first_name"))
                .as("First Name")
                .isEqualTo("Michael");
        softly.assertThat(firstUser.get("last_name"))
                .as("Last Name")
                .isEqualTo("Lawson");
        softly.assertThat(firstUser.get("email"))
                .as("Email")
                .isEqualTo("michael.lawson@reqres.in");
        softly.assertThat(firstUser.get("avatar"))
                .as("Avatar URL")
                .isEqualTo("https://reqres.in/img/faces/7-image.jpg");
        softly.assertAll();
    }

    /**
     * SEND:     GET https://reqres.in/api/users?page=2
     * HEADERS:  x-api-key: reqres-free-v1
     * RESPONSE:
     *    Status Code: 200 OK
     *    Body: JSON list of users (page 2)
     *
     * Purpose:
     *    - Verify that a GET request returns a 200 OK status.
     *    - Extract the first user's ID and email from the response.
     *    - Assert the values match expected data.
     */
    @Test
    public void shouldExtractUserData_whenResponseIsValid() {
        // Send GET request to fetch users on page 2
        Response response = UserApiHelper.listUsers(withApiKey, 2);

        // Ensure we receive a 200 OK response
        assertEquals(200, response.getStatusCode(), "Expected 200 OK");

        // Parse and extract the list of users from the JSON body
        List<Map<String, Object>> users = response.jsonPath().getList("data");

        // Extract the first user's data
        Map<String, Object> firstUser = users.get(0);
        int id = (int) firstUser.get("id");
        String email = (String) firstUser.get("email");

        // Print extracted details to console
        LOGGER.info("Extracted ID: " + id);
        LOGGER.info("Extracted email: " + email);

        // Validate extracted values
        assertEquals(7, id);
        assertEquals("michael.lawson@reqres.in", email);
    }

    /**
     * SEND:     GET https://reqres.in/api/users?page=1
     *           GET https://reqres.in/api/users?page=2
     * HEADERS:  x-api-key: reqres-free-v1
     * RESPONSE:
     *    Status Code: 200 OK
     *    Body: JSON list of users
     *
     * Purpose:
     *    - Extract all users from both available pages.
     *    - Sort them alphabetically by their first name.
     *    - Print the sorted list (first name, last name, email).
     *    - Assert the total number of users is 12.
     */
    @Test
    public void shouldSortUsersAlphabeticallyByFirstName() {
        List<Map<String, Object>> allUsers = new ArrayList<>();

        // Loop through page 1 and 2 to extract users
        for (int page = 1; page <= 2; page++) {
            Response response = UserApiHelper.listUsers(withApiKey, page);

            // Assert that the request returns HTTP 200
            assertEquals(200, response.getStatusCode(), "Expected 200 OK");

            // Extract list of users from JSON response
            List<Map<String, Object>> users = response.jsonPath().getList("data");
            allUsers.addAll(users);
        }

        // Sort the collected users alphabetically by first_name
        allUsers.sort(Comparator.comparing(user -> user.get("first_name").toString()));

        // Print the sorted list (for logging/visual check)
        LOGGER.info("Sorted Users by First Name: ");
        System.out.println("Sorted Users by First Name:");
        for (Map<String, Object> user : allUsers) {
            String name = user.get("first_name") + " " + user.get("last_name");
            String email = user.get("email").toString();
            LOGGER.info(" - " + name + " | " + email);
        }

        // Validate total number of users retrieved and sorted
        assertEquals(12, allUsers.size(), "Expected 12 users total");
    }

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
    public void shouldReturnUserDetails_whenFetchingById() {
        // Get first page of users and extract the first user ID
        Response listResponse = UserApiHelper.listUsers(withApiKey, 1);

        assertEquals(200, listResponse.statusCode(), "Expected 200 OK");

        int userId = listResponse.jsonPath().getInt("data[0].id");

        // Fetch specific user by ID using helper
        Response userResponse = UserApiHelper.getUserById(withApiKey, userId);
        assertEquals(200, userResponse.statusCode(), "Expected 200 OK");

        // Validate user details with soft assertions
        JsonPath jsonPath = userResponse.jsonPath();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(jsonPath.getInt("data.id"))
                .as("User ID")
                .isEqualTo(userId);
        softly.assertThat(jsonPath.getString("data.first_name"))
                .as("First Name")
                .isNotNull();
        softly.assertThat(jsonPath.getString("data.last_name"))
                .as("Last Name")
                .isNotNull();
        softly.assertThat(jsonPath.getString("data.email"))
                .as("Email")
                .contains("@reqres.in");
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
    public void shouldReturn404_whenUserIdIsInvalid() {
        int nonExistingUserId = 999;

        // Send GET request for a user that does not exist
        Response response = UserApiHelper.getUserById(withApiKey, nonExistingUserId);

        // Assert that response status is 404 Not Found
        assertEquals(404, response.statusCode(), "Expected 404 Not Found");

        // Assert that response body is an empty JSON object
        String responseBody = response.getBody().asString();
        System.out.println("Response body content: " + responseBody);
        assertEquals("{}", responseBody.trim(), "Expected empty JSON body");
    }
}
