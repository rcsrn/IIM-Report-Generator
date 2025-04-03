package com.iim.service.report.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class Properties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}

