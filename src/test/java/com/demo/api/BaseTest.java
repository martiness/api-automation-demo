package com.demo.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseTest {

    @BeforeAll
    public static void setup() {
        String baseUrl = System.getProperty("baseUrl", "https://reqres.in"); // default value
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .addHeader("x-api-key", "reqres-free-v1")
                .setContentType("application/json")
                .build();

        RestAssured.requestSpecification = requestSpecification;
        System.out.println("Base URL used: " + baseUrl);
    }
}