package com.vannguyen.java_learn_ecom.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        String name,
        String version
) {
}