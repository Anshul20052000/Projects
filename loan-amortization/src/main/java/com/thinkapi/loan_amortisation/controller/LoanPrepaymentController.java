package com.thinkapi.loan_amortisation.controller;

import com.thinkapi.loan_amortisation.dto.loanPrepayment.LoanPrepaymentRequest;
import com.thinkapi.loan_amortisation.dto.loanPrepayment.LoanPrepaymentResponse;
import com.thinkapi.loan_amortisation.service.api.loanPrepayment.LoanPrepaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@Tag(name="Loan Prepayment",
        description="APIs related to loan prepayment processing")
@RequestMapping("/api/loan/loan-prepayment")
@Validated
public class LoanPrepaymentController {

    @Autowired
    private LoanPrepaymentService loanPrepaymentService;

    @PostMapping
    @Operation(
            summary = "Loan Prepayment",
            description = "Allows partial or full loan prepayment and recalculates EMI or tenure"
    )
    public ResponseEntity<LoanPrepaymentResponse> prepayLoan(
             @Valid @RequestBody LoanPrepaymentRequest request) {

        LoanPrepaymentResponse response = loanPrepaymentService.processPrepayment(request);
        return ResponseEntity.ok(response);
    }
}
