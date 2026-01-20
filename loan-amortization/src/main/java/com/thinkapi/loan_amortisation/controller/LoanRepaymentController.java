package com.thinkapi.loan_amortisation.controller;

import com.thinkapi.loan_amortisation.dto.loan.LoanRepaymentRequest;
import com.thinkapi.loan_amortisation.dto.loan.LoanRepaymentResponse;
import com.thinkapi.loan_amortisation.service.api.loanRepayment.LoanRepaymentService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Hidden
@RestController
@RequestMapping("/api/loan/repaymentSchedule")
public class LoanRepaymentController {

    private static final Logger logger = LoggerFactory.getLogger(LoanRepaymentController.class);
    private final LoanRepaymentService loanRepaymentService;

    public LoanRepaymentController(LoanRepaymentService loanRepaymentService) {
        this.loanRepaymentService = loanRepaymentService;
    }

    @GetMapping("/{loanReference}")
    public ResponseEntity<LoanRepaymentResponse> getRepaymentSchedule(
            @PathVariable Long loanReference,
            @RequestParam Integer repayOption,
            @RequestParam(defaultValue = "100") Integer pageSize,
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
        logger.info("Received getRepaymentSchedule request: loanReference={}, repayOption={}, pageSize={}, pageNum={}, userId={}, entity={}",
                loanReference, repayOption, pageSize, pageNum, userId, entity);
        try {
            LoanRepaymentRequest req = new LoanRepaymentRequest();
            req.setLoanReference(loanReference);
            req.setRepayOption(repayOption);
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

            logger.info("LoanRepaymentRequest built: {}", req);
            LoanRepaymentResponse response = loanRepaymentService.getLoanRepayment(req).block();
            logger.info("LoanRepaymentResponse received: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("Error occurred while processing getRepaymentSchedule for loanReference: {}", loanReference, ex);
            logger.info("Batch log failure: loanReference={}, reason={}", loanReference, ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}

