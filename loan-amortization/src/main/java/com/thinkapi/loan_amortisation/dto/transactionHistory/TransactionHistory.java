package com.thinkapi.loan_amortisation.dto.transactionHistory;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionHistory {
    private String entityCode;
    private String transactionDateTime;
    private BigDecimal netBalance;
    private String accountCurrency;
    private String transactionRef;
    private String transactionDescription;
    private String valueDate;
    private String transactionDate;
    private String chequeNumber;
    private String extensibilityMap;
    private Long transactionId;
    private Long transactionType;
    private Long debitCreditFlag;
    private BigDecimal movementAmount;
    private String txnDscrptn4;
}
