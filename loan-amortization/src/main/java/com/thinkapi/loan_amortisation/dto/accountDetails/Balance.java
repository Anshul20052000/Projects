package com.thinkapi.loan_amortisation.dto.accountDetails;

import lombok.Data;

@Data
public class Balance {
    private String dateTime;
    private BalanceAmount amount;
    private String accountReference;
    private CreditLine creditLine;
    private String type;
    private String creditDebitIndicator;
}
