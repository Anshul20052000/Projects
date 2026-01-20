package com.thinkapi.loan_amortisation.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI loanOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Loan Amortization Improvement API")
                        .description(
                                "APIs to calculate loan amortization, " +
                                        "suggest EMI improvements based on salary, expenses, and savings."
                        )
                        .version("v1.0.0")
                );
    }

}
