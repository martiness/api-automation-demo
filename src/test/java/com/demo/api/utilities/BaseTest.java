package com.demo.api.utilities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.qameta.allure.Allure;
import io.qameta.allure.junit5.AllureJunit5;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@ExtendWith(AllureJunit5.class)
public abstract class BaseTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);

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

        // Debug log output
        LOGGER.info("Base URI: {}", baseUrl);
        LOGGER.info("Api Key: {}", apiKey);
        LOGGER.debug("[BaseTest] Setup complete before each test.");
    }

    /**
     * Attaches the execution log file to the Allure report after each test.
     *
     * <p>If a log file is found at {@code target/logs/test-info.log}, it is attached as a plain-text
     * file to the Allure report to aid debugging and traceability.</p>
     */
    @AfterEach
    void attachLogsToAllure() {
        File logFile = new File("target/logs/test-info.log");
        if (logFile.exists()) {
            try (InputStream is = new FileInputStream(logFile)) {
                Allure.addAttachment("Execution Log", "text/plain", is, ".log");
            } catch (IOException e) {
                System.err.println("Failed to attach log file to Allure report: " + e.getMessage());
            }
        }
    }
}
