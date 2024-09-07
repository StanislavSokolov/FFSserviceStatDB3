package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Settings {
    public static String getProperties(String key) throws IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("src/main/resources/hibernate.properties"))) {
            props.load(in);
        }
        String s = null;

        if (key.equals("hibernate.driver_class")) s = props.getProperty("hibernate.driver_class");
        else if (key.equals("hibernate.connection.url")) s = props.getProperty("hibernate.connection.url");
        else if (key.equals("hibernate.connection.username")) s = props.getProperty("hibernate.connection.username");
        else if (key.equals("hibernate.connection.password")) s = props.getProperty("hibernate.connection.password");
        else if (key.equals("hibernate.dialect")) s = props.getProperty("hibernate.dialect");
        else if (key.equals("hibernate.current_session_context_class")) s = props.getProperty("hibernate.current_session_context_class");
        else if (key.equals("hibernate.show_sql")) s = props.getProperty("hibernate.show_sql");
        return s;
    }
}
