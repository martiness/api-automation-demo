package com.demo.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests extends BaseTest {

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
    @Test
    public void successfulResponseTest() {
        // Send GET request to the users endpoint
        Response response = RestAssured
                .given()
                .spec(withoutApiKey)
                .queryParam("page", 2)
                .get("/api/users");

        // Verify that response status code is 200 OK
        assertEquals(200, response.getStatusCode(), "Expected 200 OK");

        // Print response body
        System.out.println("Response body :\n" + response.prettyPrint());
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
    public void userDataAssertionPageOneTest() {
        // Send GET request to the users endpoint
        Response response = RestAssured
                .given()
                .spec(withoutApiKey)
                .queryParam("page", 1)
                .when()
                .get("/api/users");

        // Verify that response status code is 200 OK
        assertEquals(200, response.getStatusCode(), "Expected 200 OK");

        // Extract user list from response JSON
        List<Map<String, Object>> users = response.jsonPath().getList("data");
        assertEquals(6, users.size(), "Expected 6 Users");

        // Extract and validate first user details
        Map<String, Object> firstUser = users.get(0);

        // Use soft assertions to validate all fields without failing immediately
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(firstUser.get("first_name")).isEqualTo("George");
        softly.assertThat(firstUser.get("last_name")).isEqualTo("Bluth");
        softly.assertThat(firstUser.get("email")).isEqualTo("george.bluth@reqres.in");
        softly.assertThat(firstUser.get("avatar")).isEqualTo("https://reqres.in/img/faces/1-image.jpg");
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
    public void userDataAssertionPageTwoTest() {
        // Send GET request to fetch users on page 2
        Response response = RestAssured
                .given()
                .spec(withoutApiKey)
                .queryParam("page", 2)
                .when()
                .get("/api/users");

        // Validate the response status code
        assertEquals(200, response.getStatusCode(), "Expected 200 OK");

        // Extract the list of users from the JSON response
        List<Map<String, Object>> users = response.jsonPath().getList("data");
        assertEquals(6, users.size(), "Expected 6 Users");

        // Access the first user in the list
        Map<String, Object> firstUser = users.get(0);

        // Use soft assertions to check all relevant user fields
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(firstUser.get("first_name")).isEqualTo("Michael");
        softly.assertThat(firstUser.get("last_name")).isEqualTo("Lawson");
        softly.assertThat(firstUser.get("email")).isEqualTo("michael.lawson@reqres.in");
        softly.assertThat(firstUser.get("avatar")).isEqualTo("https://reqres.in/img/faces/7-image.jpg");
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
    public void userDataExtractionTest() {
        // Send GET request to fetch users on page 2
        Response response = RestAssured
                .given()
                .spec(withoutApiKey)
                .queryParam("page", 2)
                .when()
                .get("/api/users");

        // Ensure we receive a 200 OK response
        assertEquals(200, response.getStatusCode(), "Expected 200 OK");

        // Parse and extract the list of users from the JSON body
        List<Map<String, Object>> users = response.jsonPath().getList("data");

        // Extract the first user's data
        Map<String, Object> firstUser = users.get(0);
        int id = (int) firstUser.get("id");
        String email = (String) firstUser.get("email");

        // Print extracted details to console
        System.out.println("Extracted ID: " + id);
        System.out.println("Extracted Email: " + email);

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
    public void extractAndSortUsersByFirstName() {
        List<Map<String, Object>> allUsers = new ArrayList<>();

        // Loop through page 1 and 2 to extract users
        for (int page = 1; page <= 2; page++) {
            Response response = RestAssured
                    .given()
                    .spec(withoutApiKey)
                    .queryParam("page", page)
                    .when()
                    .get("/api/users");

            // Assert that the request returns HTTP 200
            assertEquals(200, response.getStatusCode(), "Expected 200 OK");

            // Extract list of users from JSON response
            List<Map<String, Object>> users = response.jsonPath().getList("data");
            allUsers.addAll(users);
        }

        // Sort the collected users alphabetically by first_name
        allUsers.sort(Comparator.comparing(user -> user.get("first_name").toString()));

        // Print the sorted list (for logging/visual check)
        System.out.println("Sorted Users by First Name:");
        for (Map<String, Object> user : allUsers) {
            String name = user.get("first_name") + " " + user.get("last_name");
            String email = user.get("email").toString();
            System.out.println(" - " + name + " | " + email);
        }

        // Validate total number of users retrieved and sorted
        assertEquals(12, allUsers.size(), "Expected 12 users total");
    }
}
