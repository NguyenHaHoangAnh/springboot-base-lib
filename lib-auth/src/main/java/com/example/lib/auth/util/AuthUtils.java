package com.example.lib.auth.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "auth")
public class AuthUtils {
    private String tokenUrl;

    private String loginUrl;

    private String[] permitUrls;
}
