package com.thinkapi.loan_amortisation.controller;

import com.thinkapi.loan_amortisation.dto.transactionHistory.TransactionHistoryResponse;
import com.thinkapi.loan_amortisation.dto.transactionHistory.TransactionHistoryRequest;
import com.thinkapi.loan_amortisation.service.api.transactionHistory.TransactionHistoryService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Hidden
@RestController
@RequestMapping("/api/account/transactionHistory")
public class TransactionHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionHistoryController.class);
    private final TransactionHistoryService transactionHistoryService;

    public TransactionHistoryController(TransactionHistoryService transactionHistoryService) {
        this.transactionHistoryService = transactionHistoryService;
    }

    @GetMapping("/{accountReference}")
    public ResponseEntity<TransactionHistoryResponse> getTransactionHistory(
            @PathVariable Long accountReference,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) BigDecimal fromAmount,
            @RequestParam(required = false) BigDecimal toAmount,
            @RequestParam(required = false) Integer transactionType,
            @RequestParam(required = false) Integer transactionAccountType,
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String movementDescription,
            @RequestParam(required = false) String extensibilityMap,
            @RequestParam(defaultValue = "22") Integer pageSize,
            @RequestParam(defaultValue = "1") Integer pageNum,
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
        logger.info("Received getTransactionHistory request: accountReference={}, fromDate={}, toDate={}, customerId={}, fromAmount={}, toAmount={}, transactionType={}, transactionAccountType={}, transactionId={}, movementDescription={}, extensibilityMap={}, pageSize={}, pageNum={}, userId={}, entity={}",
                accountReference, fromDate, toDate, customerId, fromAmount, toAmount, transactionType, transactionAccountType, transactionId, movementDescription, extensibilityMap, pageSize, pageNum, userId, entity);
        try {
            TransactionHistoryRequest req = new TransactionHistoryRequest();
            req.setAccountReference(accountReference);
            req.setFromDate(fromDate);
            req.setToDate(toDate);
            req.setCustomerId(customerId);
            req.setFromAmount(fromAmount);
            req.setToAmount(toAmount);
            req.setTransactionType(transactionType);
            req.setTransactionAccountType(transactionAccountType);
            req.setTransactionId(transactionId);
            req.setMovementDescription(movementDescription);
            req.setExtensibilityMap(extensibilityMap);
            req.setPageSize(pageSize);
            req.setPageNum(pageNum);
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

            logger.info("TransactionHistoryRequest built: {}", req);
            TransactionHistoryResponse response = transactionHistoryService.getTransactionHistory(req).block();
            logger.info("TransactionHistoryResponse received: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("Error occurred while processing getTransactionHistory for accountReference: {}", accountReference, ex);
            logger.info("Batch log failure: accountReference={}, reason={}", accountReference, ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}

