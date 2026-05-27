package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyProvider {

    private static final PropertyProvider INSTANCE = new PropertyProvider();
    private final Properties properties = new Properties();

    public static PropertyProvider getInstance() {
        return INSTANCE;
    }

    private PropertyProvider() {
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config", e);
        }
    }

    public String getToken() {
        return properties.getProperty("yandex.auth.token");
    }

    public String getBaseUrl() {
        return properties.getProperty("yandex.url");
    }

    public String getLogin() {
        return properties.getProperty("yandex.login");
    }

    public String getDisplayName() {
        return properties.getProperty("yandex.display_name");
    }
}
