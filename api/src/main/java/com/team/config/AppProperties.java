package com.team.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final File file = new File();

    @Getter
    @Setter
    public static class Auth {
        private String secret;
        private long expirationTime;
    }

    @Getter
    @Setter
    public static class File {
        private String dir;
    }
}