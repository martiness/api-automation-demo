package com.demo.api.utilities;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Utility class for sending requests to the /api/users endpoint.
 * Provides reusable methods for common API operations (CRUD).
 *
 * <p> The appropriate {@link RequestSpecification} (e.g., withApiKey, withoutApiKey)
 * must be provided as a parameter to each method.</p>
 */
public class UserApiHelper {

    /**
     * Sends a POST request to create a new user.
     *
     * @param spec    the request specification (e.g., withApiKey)
     * @param payload a map containing "name" and "job"
     * @return the API response
     */
    public static Response createUser(RequestSpecification spec, Map<String, String> payload) {
        return given()
                .spec(spec)
                .body(payload)
                .post("/api/users");
    }

    /**
     * Sends a DELETE request to remove a user by ID.
     *
     * @param spec   the request specification
     * @param userId the user ID to delete
     * @return the API response
     */
    public static Response deleteUserById(RequestSpecification spec, String userId) {
        return given()
                .spec(spec)
                .delete("/api/users/" + userId);
    }

    /**
     * Sends a GET request to retrieve a user by ID.
     *
     * @param spec the request specification
     * @param id   the user ID
     * @return the API response
     */
    public static Response getUserById(RequestSpecification spec, int id) {
        return given()
                .spec(spec)
                .get("/api/users/" + id);
    }

    /**
     * Sends a GET request to retrieve a paginated list of users.
     *
     * @param spec the request specification
     * @param page the page number
     * @return the API response
     */
    public static Response listUsers(RequestSpecification spec, int page) {
        return given()
                .spec(spec)
                .queryParam("page", page)
                .get("/api/users");
    }
}
