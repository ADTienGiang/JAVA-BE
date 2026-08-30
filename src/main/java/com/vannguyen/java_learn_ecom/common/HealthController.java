package com.vannguyen.java_learn_ecom.common;

import com.vannguyen.java_learn_ecom.common.api.ApiResponse;
import com.vannguyen.java_learn_ecom.common.config.AppProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final AppProperties appProperties;

    public HealthController(
            AppProperties appProperties
    ) {
        this.appProperties = appProperties;
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> health() {
        Map<String, String> data = Map.of(
                "name", appProperties.name(),
                "version", appProperties.version(),
                "status", "UP"
        );

        return ResponseEntity.ok(ApiResponse.success(data));
    }
}