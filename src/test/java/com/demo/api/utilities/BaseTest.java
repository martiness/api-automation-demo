package com.demo.api.utilities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;

/**
 * Abstract base class for configuring reusable REST-assured request specifications.
 *
 * <p>This class defines two common {@link RequestSpecification} objects:</p>
 * <ul>
 *     <li><strong>withApiKey</strong> – used for authenticated API requests (e.g. POST, PUT, DELETE).</li>
 *     <li><strong>withoutApiKey</strong> – used for public or negative test scenarios (e.g. GET).</li>
 * </ul>
 *
 * <p>Configuration values such as the base URI and API key are loaded from {@code config.properties}
 * using the {@link Config} utility class.</p>
 *
 * <p>All test classes should extend this base class to reuse and standardize request configurations.</p>
 */
public abstract class BaseTest {

    /** Request specification for endpoints that require authentication via API key. */
    protected RequestSpecification withApiKey;

    /** Request specification for endpoints that do not require authentication. */
    protected RequestSpecification withoutApiKey;

    /**
     * Initializes both request specifications before each test.
     * This ensures consistent and reusable setup across all test cases.
     */
    @BeforeEach
    public void setup() {
        // Load configuration values
        String baseUrl = Config.getBaseUri();
        String apiKey = Config.getApiKey();

        // Specification with API key for authenticated requests
        withApiKey = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .addHeader("x-api-key", apiKey)
                .setContentType("application/json")
                .build();

        // Specification without API key for unauthenticated or negative tests
        withoutApiKey = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType("application/json")
                .build();

        // Debug output (optional)
        System.out.println("Base URL used: " + baseUrl);
        System.out.println("[BaseTest] Setup complete before each test.");
    }
}
