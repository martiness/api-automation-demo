package com.demo.api.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserApiHelper.class);

    /**
     * Sends a POST request to create a new user.
     *
     * @param spec    the request specification (e.g., withApiKey)
     * @param payload a map containing "name" and "job"
     * @return the API response
     */
    public static Response createUser(RequestSpecification spec, Map<String, String> payload) {

        LOGGER.info("Creating user with payload: {}", payload);

        Response response = RestAssured
                .given()
                .spec(spec)
                .body(payload)
                .post("/api/users");

        if (response.getStatusCode() >= 400) {
            LOGGER.warn("Create user failed. Status: {}, Body: {}", response.getStatusCode(), response.asString());
        } else {
            LOGGER.debug("Response received: {}", response.asString());
        }

        return response;
    }

    /**
     * Sends a DELETE request to remove a user by ID.
     *
     * @param spec   the request specification
     * @param userId the user ID to delete
     * @return the API response
     */
    public static Response deleteUserById(RequestSpecification spec, String userId) {

        LOGGER.info("Deleting user with id: {}", userId);

        Response response = RestAssured
                .given()
                .spec(spec)
                .delete("/api/users/" + userId);

        LOGGER.debug("Response received: {}", response.asString());

        return response;
    }

    /**
     * Sends a GET request to retrieve a user by ID.
     *
     * @param spec the request specification
     * @param id   the user ID
     * @return the API response
     */
    public static Response getUserById(RequestSpecification spec, int id) {
        LOGGER.info("Getting user with id: {}", id);

        Response response = RestAssured
                .given()
                .spec(spec)
                .get("/api/users/" + id);

        LOGGER.debug("Response received: {}", response.asString());

        return response;
    }

    /**
     * Sends a GET request to retrieve a paginated list of users.
     *
     * @param spec the request specification
     * @param page the page number
     * @return the API response
     */
    public static Response listUsers(RequestSpecification spec, int page) {

        LOGGER.info("Listing users with page {}", page);

        Response response = RestAssured
                .given()
                .spec(spec)
                .queryParam("page", page)
                .get("/api/users");

        LOGGER.debug("Response received: {}", response.asString());

        return response;
    }
}
