
package com.thinkapi.loan_amortisation.dto.accountDetails;
import lombok.Data;

@Data
public class AccountBalanceResponse {
    private Long noOfPages;
    private AccountBalanceDetails accountBalanceDetails;
    private Long totalNoOfRecords;
    private Long pageSize;
    private String hasNext;
    private Long pageNum;
}

