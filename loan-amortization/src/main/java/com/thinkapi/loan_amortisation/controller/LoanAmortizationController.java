package com.thinkapi.loan_amortisation.controller;

import com.thinkapi.loan_amortisation.dto.loanAmortization.LoanAmortizationRequest;
import com.thinkapi.loan_amortisation.dto.loanAmortization.LoanAmortizationResponse;
import com.thinkapi.loan_amortisation.exception.InvalidInputException;
import com.thinkapi.loan_amortisation.service.api.loanAmortization.LoanAmortizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@RestControllerAdvice
@SecurityRequirement(name="bearerAuth")
@RestController
@Tag(name="Loan Amortization",
        description="APIs related to loan amortization calculation and improvement")
@RequestMapping("api/loan")
public class LoanAmortizationController {

    private static final Logger logger = LoggerFactory.getLogger(LoanAmortizationController.class);
    private final LoanAmortizationService loanAmortizationService;

    public LoanAmortizationController(LoanAmortizationService loanAmortizationService) {
        this.loanAmortizationService = loanAmortizationService;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan amortization calculated successfully"),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"timestamp\": \"2025-12-14T22:11:30\", \"error\": \"Not Found\", \"details\": \"Requested resource does not exist\", \"status\": 404, \"success\": false }"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"timestamp\": \"2025-12-14T22:10:15\", \"error\": \"Unauthorized\", \"details\": \"JWT token is missing or invalid\", \"status\": 401, \"success\": false }"))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"timestamp\": \"2025-12-14T22:10:45\", \"error\": \"Forbidden\", \"details\": \"You do not have permission to access this resource\", \"status\": 403, \"success\": false }"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"timestamp\": \"2025-12-14T22:08:08\", \"error\": \"Internal Server Error\", \"details\": \"Index 32 out of bounds for length 32\", \"status\": 500, \"success\": false }")))
    })
    @Operation(
            summary = "Get loan amortization schedule",
            description = "Returns suggested amortization schedule including principal, interest and outstanding balance"
    )
    @GetMapping("/loanAmortization")
    public ResponseEntity<LoanAmortizationResponse> getAmortizationEmi(
            @RequestParam Long loanReference,
            @RequestParam Long accountReference,
            @RequestParam BigDecimal oldSalary,
            @RequestParam BigDecimal newSalary,
            @RequestParam BigDecimal balanceCushion,
            @RequestParam(required = false) Integer installmentPaid
    ) {
        logger.info("Received getAmortizationEmi request: loanReference={}, accountReference={}, newSalary={}, balanceCushion={}, installmentPaid={}",
                loanReference, accountReference, newSalary, balanceCushion, installmentPaid);
        // try {
            if(loanReference < 0) {
                throw new InvalidInputException("Loan reference cannot be negative.");
            }
            if(accountReference < 0) {
                throw new InvalidInputException("Account reference cannot be negative.");
            }
            if(oldSalary.compareTo(new BigDecimal("0.0")) < 0){
                throw new InvalidInputException("Old salary cannot be negative.");
            }
            if(newSalary.compareTo(new BigDecimal("0.0")) < 0){
                throw new InvalidInputException("New salary cannot be negative.");
            }
            if(balanceCushion.compareTo(new BigDecimal("0.0")) < 0){
                throw new InvalidInputException("Balance cushion cannot be negative.");
            }
            if(installmentPaid!=null && installmentPaid < 0) {
                throw new InvalidInputException("Installment paid cannot be negative.");
            }

            LoanAmortizationRequest req = new LoanAmortizationRequest();
            req.setLoanReference(loanReference);
            req.setAccountReference(accountReference);
            req.setOldSalary(oldSalary);
            req.setNewSalary(newSalary);
            req.setBalanceCushion(balanceCushion);
            req.setInstallmentPaid((installmentPaid));

            logger.debug("LoanAmortizationRequest built: {}", req);
            LoanAmortizationResponse response = loanAmortizationService.getAmortization(req);
            logger.info("LoanAmortizationResponse returned with Status - 200 OK: {}", response);
            return ResponseEntity.ok(response);
        // } catch (Exception ex) {
            // logger.debug(ex.toString());
            // logger.error("Error occurred while processing getAmortizationEmi for loanReference: {}", loanReference, ex);
            // logger.info("Batch log failure: loanReference={}, reason={}", loanReference, ex.getMessage());
            // return ResponseEntity.internalServerError().build();
        // }
    }
}
