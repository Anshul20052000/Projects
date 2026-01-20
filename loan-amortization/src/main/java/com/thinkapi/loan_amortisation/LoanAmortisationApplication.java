package com.thinkapi.loan_amortisation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class LoanAmortisationApplication {

    private static final Logger logger = LoggerFactory.getLogger(LoanAmortisationApplication.class);

	public static void main(String[] args) {
        logger.debug("Starting LoanAmortisationApplication in debug mode");
        try {
            SpringApplication.run(LoanAmortisationApplication.class, args);
        } catch (Exception e) {
            logger.error("Application failed to start", e);
        }
	}

}
