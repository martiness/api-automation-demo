package com.demo.api.untilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for loading configuration values from a properties file.
 *
 * <p>This class reads the {@code config.properties} file located in {@code src/test/resources}
 * and provides access to commonly used configuration keys like base URI and API key.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     String baseUri = Config.getBaseUri();
 *     String apiKey = Config.getApiKey();
 * </pre>
 *
 * <p>If the configuration file cannot be loaded or a required key is missing, the class throws a runtime exception.</p>
 */
public class Config {

    // Static Properties object to hold loaded configuration values.
    private static final Properties PROPERTIES = new Properties();

    // Static initializer block to load the config.properties file when the class is first used.
    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Cannot load config.properties file");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file", e);
        }
    }

    /**
     * Returns the base URI used in REST API tests.
     *
     * @return the base URI string from config.properties (e.g. "https://reqres.in")
     */
    public static String getBaseUri() {
        return PROPERTIES.getProperty("base.uri");
    }

    /**
     * Returns the API key used in authenticated requests.
     *
     * @return the API key string from config.properties (e.g. "reqres-free-v1")
     */
    public static String getApiKey() {
        return PROPERTIES.getProperty("api.key");
    }
}
