package com.thinkapi.loan_amortisation.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Tag(name = "Authentication & Token Management",
        description = "APIs for generating and managing access tokens used to authorize secured services")
@RestController
@RequestMapping("/demo")
public class TokenController {

    @Operation(
            summary = "Generate access token",
            description = "Generates a JWT access token that must be used in Authorization header to access secured loan and account APIs"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token generated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"timestamp\": \"2025-12-14T22:10:15\", \"error\": \"Unauthorized\", \"details\": \"JWT token is missing or invalid\", \"status\": 401, \"success\": false }"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"timestamp\": \"2025-12-14T22:08:08\", \"error\": \"Internal Server Error\", \"details\": \"Index 32 out of bounds for length 32\", \"status\": 500, \"success\": false }")))
    })
    @GetMapping("/generate-token")
    public String generateToken(@RequestParam String username) {
        String secret = "MYSUPERLONGSECRETKEY1234567890ABCDMYSUPERLONGSECRETKEY1234567890ABCD"; // same as application.properties
        SecretKey key = new SecretKeySpec(secret.getBytes(),"HmacSHA256");
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hour
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
