package com.thinkapi.loan_amortisation.service.api.loanPrepayment;

import com.thinkapi.loan_amortisation.dto.loan.LoanRepaymentRequest;
import com.thinkapi.loan_amortisation.dto.loan.LoanRepaymentResponse;
import com.thinkapi.loan_amortisation.dto.loan.RepaymentEvent;
import com.thinkapi.loan_amortisation.dto.loanPrepayment.*;
import com.thinkapi.loan_amortisation.exception.InvalidInputException;
import com.thinkapi.loan_amortisation.service.api.loanRepayment.LoanRepaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class LoanPrepaymentService {

    private static final int SCALE = 10;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final LoanRepaymentService loanRepaymentService;

    public LoanPrepaymentService(LoanRepaymentService loanRepaymentService) {
        this.loanRepaymentService = loanRepaymentService;
    }

    public LoanPrepaymentResponse processPrepayment(LoanPrepaymentRequest req) {

        // Logic to process Prepayment

        return new LoanPrepaymentResponse();
    }

}
