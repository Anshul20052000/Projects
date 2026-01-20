package com.thinkapi.loan_amortisation.dto.loan;

import lombok.Data;

@Data
public class LoanRepaymentRequest {

    // Path variable
    private Long loanReference;

    // Query params
    private Integer repayOption=1;   // 1-default, 2-future
    private Integer pageSize = 32; // default
    private Integer pageNum = 1;   // default

    // Headers
    private Integer userId=1;
    private String entity="GPRDTTSTOU";
    private Integer languageCode=1;
    private Integer channelType;
    private Integer coRelationId;
    private Integer uuidSeqNo;
    private String initiatingSystem;
    private Integer serviceMode;
    private String accessToken="/home/marketplace/TCS_BaNCS_Marketplace/Bancs/gateway_files/azure/_app_keys.html.erb\n";
    private String referenceId;
}

