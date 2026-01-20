package com.thinkapi.loan_amortisation.dto.loanAmortization;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonPropertyOrder({
        "currentEmi",
        "suggestedEmi",
        "currentAmortizedValue",
        "newAmortizedValue",
        "outstandingPrincipal",
        "currentNoOfInstallments",
        "newNoOfInstallments",
        "monthlyExpenses",
        "savingsOnInterest",
        "monthlyIncome",
        "monthlySavings",
        "newSchedule"
})
public class LoanAmortizationResponse {

    @Schema(description = "Current EMI amount", example = "1500.50")
    private BigDecimal currentEmi;

    @Schema(description = "Suggested EMI amount based on new inputs", example = "1400.75")
    private BigDecimal suggestedEmi;

    @Schema(description = "Current amortized loan value", example = "50000.00")
    private BigDecimal currentAmortizedValue;

    @Schema(description = "New amortized loan value after recalculation", example = "48000.00")
    private BigDecimal newAmortizedValue;

    @Schema(description = "Outstanding principal amount of the loan", example = "30000.00")
    private BigDecimal outstandingPrincipal;

    @Schema(description = "Current number of installments paid", example = "24")
    private Long currentNoOfInstallments;

    @Schema(description = "New number of installments after recalculation", example = "22")
    private Long newNoOfInstallments;

    @Schema(description = "Monthly expenses of the borrower", example = "5000.00")
    private BigDecimal monthlyExpenses;

    @Schema(description = "Savings on interest due to new EMI schedule", example = "2000.00")
    private BigDecimal savingsOnInterest;

    @Schema(description = "Monthly income of the borrower", example = "60000.00")
    private BigDecimal monthlyIncome;

    @Schema(description = "Monthly savings after EMI and expenses", example = "1000.00")
    private BigDecimal monthlySavings;

    @Schema(description = "Detailed amortization schedule")
    private List<AmortizedSchedule> newSchedule;
}
