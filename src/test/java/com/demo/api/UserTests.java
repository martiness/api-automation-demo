package com.demo.api;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests extends BaseTest {

    @Test
    public void successfulResponseTest() {
        // Create request
        RequestSpecification request = RestAssured
                .given()
                .queryParam("page", 2);

        // Send get request
        Response response = request.get("/api/users");

        // Verification for successful response
        assertEquals(200, response.getStatusCode(), "Expected 200 OK");

        // Print response body
        System.out.println("Response body :\n" + response.prettyPrint());
    }

    @Test
    public void userDataAssertionPageOneTest() {
        // Create request
        RequestSpecification request = RestAssured
                .given()
                .queryParam("page", 1);

        // Send get request
        Response response = request.get("/api/users");

        // Verification for successful response
        assertEquals(200, response.getStatusCode(), "Expected 200 OK");

        List<Map<String, Object>> users = response.jsonPath().getList("data");;
        assertEquals(6, users.size(), "Expected 6 Users");

        Map<String, Object> firstUser = users.get(0);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(firstUser.get("first_name")).isEqualTo("George");
        softly.assertThat(firstUser.get("last_name")).isEqualTo("Bluth");
        softly.assertThat(firstUser.get("email")).isEqualTo("george.bluth@reqres.in");
        softly.assertThat(firstUser.get("avatar")).isEqualTo("https://reqres.in/img/faces/1-image.jpg");
        softly.assertAll();
    }

    @Test
    public void userDataAssertionPageTwoTest() {
        // Create request
        RequestSpecification request = RestAssured
                .given()
                .queryParam("page", 2);

        // Send get request
        Response response = request.get("/api/users");

        // Verification for successful response
        assertEquals(200, response.getStatusCode(), "Expected 200 OK");

        List<Map<String, Object>> users = response.jsonPath().getList("data");;
        assertEquals(6, users.size(), "Expected 6 Users");

        Map<String, Object> firstUser = users.get(0);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(firstUser.get("first_name")).isEqualTo("Michael");
        softly.assertThat(firstUser.get("last_name")).isEqualTo("Lawson");
        softly.assertThat(firstUser.get("email")).isEqualTo("michael.lawson@reqres.in");
        softly.assertThat(firstUser.get("avatar")).isEqualTo("https://reqres.in/img/faces/7-image.jpg");
        softly.assertAll();
    }

    @Test
    public void userDataExtractionTest() {
        // Create request
        RequestSpecification request = RestAssured
                .given()
                .queryParam("page", 2);

        // Send get request
        Response response = request.get("/api/users");

        // Verification for successful response
        assertEquals(200, response.getStatusCode(), "Expected 200 OK");

        // Extract list of users
        List<Map<String, Object>> users = response.jsonPath().getList("data");

        // Work with first user
        Map<String, Object> firstUser = users.get(0);
        int id = (int) firstUser.get("id");
        String email = (String) firstUser.get("email");

        // Print values
        System.out.println("Extracted ID: " + id);
        System.out.println("Extracted Email: " + email);

        // Assertions
        assertEquals(7, id);
        assertEquals("michael.lawson@reqres.in", email);
    }

    @Test
    public void extractAndSortUsersByFirstName() {

        // Prepare reusable request specification
        RequestSpecification request = RestAssured
                .given()
                .header("Accept", "application/json");

        List<Map<String, Object>> allUsers = new ArrayList<>();

        // Extract users from both pages
        for (int page = 1; page <= 2; page++) {
            Response response = request
                    .queryParam("page", page)
                    .when()
                    .get("/api/users");

            assertEquals(200, response.getStatusCode(), "Expected 200 OK");

            List<Map<String, Object>> users = response.jsonPath().getList("data");
            allUsers.addAll(users);
        }

        // Sort by first_name
        allUsers.sort(Comparator.comparing(user -> user.get("first_name").toString()));

        // Print sorted result
        System.out.println("Sorted Users by First Name:");
        for (Map<String, Object> user : allUsers) {
            String name = user.get("first_name") + " " + user.get("last_name");
            String email = user.get("email").toString();
            System.out.println(" - " + name + " | " + email);
        }

        assertEquals(12, allUsers.size(), "Expected 12 users total");
    }



}
