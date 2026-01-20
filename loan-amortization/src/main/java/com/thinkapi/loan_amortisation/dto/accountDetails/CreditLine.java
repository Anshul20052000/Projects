package com.thinkapi.loan_amortisation.dto.accountDetails;

import lombok.Data;

@Data
public class CreditLine {
    private CreditLineAmount amount;
    private String balanceType;
    private String included;
}
