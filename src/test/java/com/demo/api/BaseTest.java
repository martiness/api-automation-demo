package com.demo.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;


public abstract class BaseTest {

    @BeforeAll
    public static void setup(){
        // Set base URI and API key
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .addHeader("x-api-key", "reqres-free-v1")
                .build();

        RestAssured.requestSpecification = requestSpecification;
        System.out.println("BaseTest setup running...");
    }
}