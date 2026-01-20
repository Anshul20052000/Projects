package com.thinkapi.loan_amortisation.dto.accountDetails;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceAmount {
    private String balanceAmountCurrency;
    private BigDecimal accountBalance;
}
