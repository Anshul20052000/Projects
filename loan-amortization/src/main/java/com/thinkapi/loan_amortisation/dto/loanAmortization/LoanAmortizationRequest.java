package com.thinkapi.loan_amortisation.dto.loanAmortization;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanAmortizationRequest {

    // Request params
    @NotNull(message = "Loan reference cannot be null")
    @Schema(description = "Reference ID of the loan", example = "123456789")
    private Long loanReference;

    @NotNull(message = "Account reference cannot be null")
    @Schema(description = "Reference ID of the account linked to the loan", example = "987654321")
    private Long accountReference;

    @NotNull(message = "Previous monthly salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be positive")
    @Schema(description = "Previous monthly salary of the user", example = "56000")
    private BigDecimal oldSalary;

    @NotNull(message = "Updated monthly salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be positive")
    @Schema(description = "Updated monthly salary of the user", example = "56000")
    private BigDecimal newSalary;

    @NotNull(message = "Minimum account balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance cushion cannot be negative")
    @Schema(description = "Minimum balance cushion to keep in account", example = "5000")
    private BigDecimal balanceCushion;

    @NotNull(message = "Installments already paid cannot be null")
    @Min(value = 0, message = "Installments already paid cannot be negative")
    @Schema(description = "Number of installments already paid", example = "24")
    private Integer installmentPaid;

}

