package com.thinkapi.loan_amortisation.dto.accountDetails;

import lombok.Data;

@Data
public class AccountBalanceRequest {

    // Path variable
    private Long accountReference;

    // Headers
    private Integer userId;
    private String entity;
    private Integer languageCode;
    private Integer channelType;
    private Integer coRelationId;
    private Integer uuidSeqNo;
    private String initiatingSystem;
    private Integer serviceMode;
    private String accessToken;
    private String referenceId;

}
