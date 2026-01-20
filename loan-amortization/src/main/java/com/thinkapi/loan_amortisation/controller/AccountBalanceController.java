package com.thinkapi.loan_amortisation.controller;

import com.thinkapi.loan_amortisation.dto.accountDetails.AccountBalanceRequest;
import com.thinkapi.loan_amortisation.dto.accountDetails.AccountBalanceResponse;
import com.thinkapi.loan_amortisation.service.api.accountBalance.AccountBalanceService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Hidden
@RestController
@RequestMapping("/api/account/balance")
public class AccountBalanceController {

    private static final Logger logger = LoggerFactory.getLogger(AccountBalanceController.class);
    private final AccountBalanceService accountApiService;

    public AccountBalanceController(AccountBalanceService accountBalanceService) {
        this.accountApiService = accountBalanceService;
    }

    @GetMapping("/{accountReference}")
    public ResponseEntity<AccountBalanceResponse> getAccountBalance(
            @PathVariable Long accountReference,
            @RequestHeader Integer userId,
            @RequestHeader String entity,
            @RequestHeader Integer languageCode,
            @RequestHeader(required = false) Integer channelType,
            @RequestHeader(required = false, name = "Co-Relationid") Integer coRelationId,
            @RequestHeader(required = false, name = "UUIDSeqNo") Integer uuidSeqNo,
            @RequestHeader(required = false) String initiatingSystem,
            @RequestHeader(required = false) Integer serviceMode,
            @RequestHeader(required = false, name = "Accesstoken") String accessToken,
            @RequestHeader(required = false) String referenceId
    ) {
        logger.info("Received getAccountBalance request for accountReference: {}, userId: {}, entity: {}", accountReference, userId, entity);
        try {
            AccountBalanceRequest req = new AccountBalanceRequest();
            req.setAccountReference(accountReference);
            req.setUserId(userId);
            req.setEntity(entity);
            req.setLanguageCode(languageCode);
            req.setChannelType(channelType);
            req.setCoRelationId(coRelationId);
            req.setUuidSeqNo(uuidSeqNo);
            req.setInitiatingSystem(initiatingSystem);
            req.setServiceMode(serviceMode);
            req.setAccessToken(accessToken);
            req.setReferenceId(referenceId);

            logger.info("AccountBalanceRequest built: {}", req);
            AccountBalanceResponse response = accountApiService.getAccountBalance(req).block();
            logger.info("AccountBalanceResponse received: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("Error occurred while processing getAccountBalance for accountReference: {}", accountReference, ex);
            logger.info("Batch log failure: accountReference={}, reason={}", accountReference, ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}

