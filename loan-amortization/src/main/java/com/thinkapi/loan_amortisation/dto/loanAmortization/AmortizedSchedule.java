package com.thinkapi.loan_amortisation.dto.loanAmortization;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Data
public class AmortizedSchedule {

    @Schema(description = "Unique identifier of the loan", example = "123456789")
    private Long loanId;

    @Schema(description = "Total installment amount for the period", example = "1500.00")
    private BigDecimal installmentAmount;

    @Schema(description = "Principal component of the installment", example = "1200.00")
    private BigDecimal principalAmount;

    @Schema(description = "Expected principal after installment payment", example = "28800.00")
    private BigDecimal expectedPrincipal;

    @Schema(description = "Projected interest amount for the period", example = "300.00")
    private BigDecimal projectedInterestAmount;

    @Schema(description = "Installment due date (YYYY-MM-DD)", example = "2025-01-10")
    private String dueDate;
}
