package com.thinkapi.loan_amortisation.dto.loan;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RepaymentEvent {
    private BigDecimal penalPaid;
    private BigDecimal installmentAmt;
    private String originalDueDt;
    private BigDecimal principalPaid;
    private BigDecimal projectedIntAmt;
    private String dueDt;
    private BigDecimal interestDue;
    private BigDecimal penalDueAmount;
    private String paidDt;
    private BigDecimal principalAmt;
    private Long loanId;
    private BigDecimal interestPaid;
    private BigDecimal expectedPrincipal;
    private Long status;
}
