package ru.evillcat.server.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private static final Logger log = LoggerFactory.getLogger(PropertiesReader.class);
    private static final String PROPERTIES_FILE_NAME = "server.properties";
    private final Properties properties;

    public PropertiesReader() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                log.error("Properties reading error.");
                System.exit(1);
            }
        } catch (IOException e) {
            log.error("Couldn't find properties file.", e.getCause());
        }
    }

    public String getProperty(String propertyKey) {
        return properties.getProperty(propertyKey);
    }
}
