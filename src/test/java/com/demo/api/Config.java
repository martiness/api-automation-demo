package com.demo.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Cannot load config.properties file");
            }
            PROPERTIES.load(input);
        }  catch (IOException e) {
            throw new RuntimeException("Failed to load config file", e);
        }
    }

    public static String getBaseUri() {
        return PROPERTIES.getProperty("base.uri");
    }

    public static String getApiKey() {
        return PROPERTIES.getProperty("api.key");
    }
}
