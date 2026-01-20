package com.thinkapi.loan_amortisation.dto.accountDetails;

import lombok.Data;

@Data
public class AccountBalanceDetails {
    private Balance balance;
    private String extensibilityMap;
}
