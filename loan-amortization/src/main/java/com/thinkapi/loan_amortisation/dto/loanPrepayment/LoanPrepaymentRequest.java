package com.thinkapi.loan_amortisation.dto.loanPrepayment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanPrepaymentRequest {

    @NotNull(message = "Loan reference is mandatory")
    @Schema(description = "Unique loan reference number", example = "LN123456789")
    private Long loanReference;

    @NotNull(message = "Account reference is mandatory")
    @Schema(description = "Account from which prepayment amount will be debited", example = "ACC99887766")
    private Long accountReference;

    @NotNull(message = "Prepayment amount is required")
    @DecimalMin(value = "1.0", inclusive = false, message = "Prepayment amount must be greater than zero")
    @Schema(description = "Lump sum prepayment amount", example = "50000")
    private BigDecimal prepaymentAmount;

    @NotNull(message = "Prepayment type is required")
    @Schema(description = "PARTIAL or FULL prepayment", example = "PARTIAL")
    private PrepaymentType prepaymentType;

    @NotNull(message = "Prepayment strategy is required")
    @Schema(description = "How loan should be adjusted after prepayment", example = "REDUCE_TENURE")
    private PrepaymentStrategy strategy;

    @Schema(description = "Date on which prepayment is applied", example = "2025-01-15")
    private LocalDate effectiveDate;

    @Schema(description = "Optional remarks", example = "Bonus payment")
    private String remarks;
}
