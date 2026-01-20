package com.thinkapi.loan_amortisation.dto.accountDetails;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditLineAmount {
    private String currency;
    private BigDecimal creditAmount;
}
