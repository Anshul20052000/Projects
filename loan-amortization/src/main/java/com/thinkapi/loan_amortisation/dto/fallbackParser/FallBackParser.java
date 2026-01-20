package com.thinkapi.loan_amortisation.dto.fallbackParser;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class FallBackParser {

    public String getFallbackJson(String path) {
        try (InputStream is =
                     new ClassPathResource(path).getInputStream()) {

            return new String(is.readAllBytes(), StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Unable to read fallback JSON", e);
        }
    }
}
