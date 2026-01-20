package com.thinkapi.loan_amortisation.dto.loanPrepayment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanPrepaymentResponse {

    @Schema(description = "Loan reference number")
    private Long loanReference;

    @Schema(description = "Outstanding principal before prepayment")
    private BigDecimal oldOutstandingPrincipal;

    @Schema(description = "Outstanding principal after prepayment")
    private BigDecimal newOutstandingPrincipal;

    @Schema(description = "Old EMI before prepayment")
    private BigDecimal oldEmi;

    @Schema(description = "New EMI after prepayment (if reduced)")
    private BigDecimal newEmi;

    @Schema(description = "Old remaining installments")
    private Long oldRemainingTenure;

    @Schema(description = "New remaining installments")
    private Long newRemainingTenure;

    @Schema(description = "Interest saved due to prepayment")
    private BigDecimal interestSaved;

    @Schema(description = "Prepayment processed successfully or not")
    private boolean success;
}
