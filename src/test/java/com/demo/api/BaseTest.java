package com.demo.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;

/**
 * Abstract base class for setting up common REST-assured configurations.
 *
 * <p>This class defines two reusable request specifications:
 * <ul>
 *     <li><strong>withApiKey</strong> – for endpoints that require authentication using an API key.</li>
 *     <li><strong>withoutApiKey</strong> – for public or negative test cases that don't require authentication.</li>
 * </ul>
 *
 * <p>Each test class that extends this base can choose the appropriate specification depending on the endpoint being tested.
 */
public abstract class BaseTest {

    /** Request specification including API key header (used for authenticated endpoints). */
    protected RequestSpecification withApiKey;

    /** Request specification without API key (used for public or unauthorized scenarios). */
    protected RequestSpecification withoutApiKey;

    /**
     * Initializes both request specifications before each test.
     * This ensures fresh and consistent configurations for every test case.
     */
    @BeforeEach
    public void setup() {
        // Retrieve base URL from system properties or fallback to default (used for flexibility in environments)
        String baseUrl = Config.getBaseUri();
        String apiKey = Config.getApiKey();

        // Specification used for endpoints that require authentication (e.g. POST, PUT, DELETE)
        withApiKey = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .addHeader("x-api-key", apiKey) // Simulated API key for demo endpoints
                .setContentType("application/json")
                .build();

        // Specification used for endpoints that don't require authentication (e.g. GET public data)
        withoutApiKey = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType("application/json")
                .build();

        // Console output for verification/debug purposes
        System.out.println("Base URL used: " + baseUrl);
        System.out.println("[BaseTest] Setup complete before each test.");
    }
}

