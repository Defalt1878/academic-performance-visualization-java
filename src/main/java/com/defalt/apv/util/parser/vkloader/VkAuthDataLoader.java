package com.defalt.apv.util.parser.vkloader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

class VkAuthDataLoader {
    public static UserAuthData loadData() {
        var properties = loadConfigurationProperties();

        final int userId =
            Integer.parseInt(Objects.requireNonNull(properties.getProperty("user_id"), "User id not found in config!"));

        final String accessToken =
            Objects.requireNonNull(properties.getProperty("access_token"), "Access token not found in config!");

        return new UserAuthData(userId, accessToken);
    }

    private static Properties loadConfigurationProperties() {
        var properties = new Properties();
        var configFile = "src/main/resources/vk_auth.properties";
        try (var fis = new FileInputStream(configFile)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Error happens during loading configuration!", e);
        }
        return properties;
    }

    public record UserAuthData(int userId, String accessToken) {
    }
}
