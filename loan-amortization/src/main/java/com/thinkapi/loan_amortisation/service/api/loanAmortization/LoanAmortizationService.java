package com.thinkapi.loan_amortisation.service.api.loanAmortization;

import com.thinkapi.loan_amortisation.dto.accountDetails.AccountBalanceRequest;
import com.thinkapi.loan_amortisation.dto.accountDetails.AccountBalanceResponse;
import com.thinkapi.loan_amortisation.dto.loan.LoanRepaymentRequest;
import com.thinkapi.loan_amortisation.dto.loan.LoanRepaymentResponse;
import com.thinkapi.loan_amortisation.dto.loan.RepaymentEvent;
import com.thinkapi.loan_amortisation.dto.loanAmortization.AmortizedSchedule;
import com.thinkapi.loan_amortisation.dto.loanAmortization.LoanAmortizationRequest;
import com.thinkapi.loan_amortisation.dto.loanAmortization.LoanAmortizationResponse;
import com.thinkapi.loan_amortisation.dto.transactionHistory.TransactionHistory;
import com.thinkapi.loan_amortisation.dto.transactionHistory.TransactionHistoryRequest;
import com.thinkapi.loan_amortisation.dto.transactionHistory.TransactionHistoryResponse;
import com.thinkapi.loan_amortisation.exception.InvalidInputException;
import com.thinkapi.loan_amortisation.service.api.accountBalance.AccountBalanceService;
import com.thinkapi.loan_amortisation.service.api.loanRepayment.LoanRepaymentService;
import com.thinkapi.loan_amortisation.service.api.transactionHistory.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanAmortizationService {
    private static final Logger logger = LoggerFactory.getLogger(LoanAmortizationService.class);
    @Autowired
    private AccountBalanceService accountBalanceService;

    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @Autowired
    private LoanRepaymentService loanRepaymentService;

    private static final int SCALE = 8;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    public LoanAmortizationResponse getAmortization(LoanAmortizationRequest req){
        try {
            logger.debug("getAmortization called with request: {}", req);
            AccountBalanceRequest accReq = new AccountBalanceRequest();
            accReq.setAccountReference(req.getAccountReference());
            AccountBalanceResponse accBalance = accountBalanceService.getAccountBalance(accReq).block();
            logger.debug("Account Balance : {}", accBalance.getAccountBalanceDetails().getBalance().getAmount().getAccountBalance());

            if (accBalance.getAccountBalanceDetails().getBalance().getAmount().getAccountBalance().compareTo(req.getBalanceCushion()) < 0){
                throw new InvalidInputException("Balance Cushion can not be less than account balance.");
            }

            TransactionHistoryRequest trhReq = new TransactionHistoryRequest();
            trhReq.setAccountReference(req.getAccountReference());
            TransactionHistoryResponse tranHistory = transactionHistoryService.getTransactionHistory(trhReq).block();
            List<TransactionHistory> listHist = tranHistory.getTransactionHistory();

            LoanRepaymentRequest repayReq = new LoanRepaymentRequest();
            repayReq.setLoanReference(req.getLoanReference());
            LoanRepaymentResponse repaySchedule = loanRepaymentService.getLoanRepayment(repayReq).block();
            logger.debug("Repay Schedule : {}", repaySchedule.getRepaymentEvents());

            List<RepaymentEvent> eventList = repaySchedule.getRepaymentEvents();
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate d1 = LocalDate.parse(eventList.get(0).getDueDt(), f);
            LocalDate d2 = LocalDate.parse(eventList.get(1).getDueDt(), f);

            long days = ChronoUnit.DAYS.between(d1,d2);
            logger.debug("Days between installments: {} (approx months: {})", days, Math.round(days/30.0));

            Integer installmentPaid = req.getInstallmentPaid();
            if(installmentPaid == null){
                for(int i=0;i<eventList.size();i++){
                    if(eventList.get(i).getPaidDt() == null){
                        installmentPaid=i+1;
                        break;
                    }
                }
            }
            BigDecimal interestRate = eventList.get(1).getProjectedIntAmt().divide(eventList.get(1).getExpectedPrincipal(), SCALE, ROUNDING);
            BigDecimal lastPrincipal = eventList.get(installmentPaid-1).getExpectedPrincipal();

            List<BigDecimal> suggestedInstallment = suggestEmiIncrease(req.getNewSalary(),
                    accBalance.getAccountBalanceDetails().getBalance().getAmount().getAccountBalance(),
                    listHist,
                    req.getBalanceCushion(),
                    Math.round(days/30.0),
                    req.getOldSalary(),
                    eventList.get(0).getInstallmentAmt(),
                    interestRate,
                    lastPrincipal
            );

            logger.debug("Returned from suggestedEmi function : {} {} {}", suggestedInstallment.get(0), suggestedInstallment.get(1), suggestedInstallment.get(2));

            LoanAmortizationResponse tempL = getAmortizedEmi(suggestedInstallment.get(1), eventList, req.getInstallmentPaid(), Math.round(days/30.0));

            tempL.setMonthlyExpenses(suggestedInstallment.get(0));
            tempL.setMonthlyIncome(req.getNewSalary());
            tempL.setMonthlySavings(((suggestedInstallment.get(2).multiply(new BigDecimal("100.0"))).divide(new BigDecimal("100.0"))).setScale(2, RoundingMode.HALF_UP));

            logger.debug("LoanAmortizationResponse built: {}", tempL);
            return tempL;
        } catch (Exception ex) {
            logger.error("Error in getAmortization for request: {}", req, ex);
            throw ex;
        }
    }

    public List<BigDecimal> suggestEmiIncrease(
            BigDecimal newSalary,
            BigDecimal acBalance,
            List<TransactionHistory> listHist,
            BigDecimal balanceCushion,
            long tenure,
            BigDecimal oldSalary,
            BigDecimal oldEmi,
            BigDecimal interestRate,
            BigDecimal lastPrincipal){
        try {
            BigDecimal debitSum= new BigDecimal("0.0");
            BigDecimal creditSum= new BigDecimal("0.0");;
            for(int i=listHist.size()-1; i>=0; i--){
                if(i<listHist.size()-30*tenure) break;
                if(listHist.get(i).getDebitCreditFlag() == 1 && listHist.get(i).getTransactionType() != 101) creditSum = creditSum.add(listHist.get(i).getMovementAmount());
                else if (listHist.get(i).getDebitCreditFlag() == 2)  debitSum = debitSum.add(listHist.get(i).getMovementAmount());
            }
            logger.debug("Inside suggestedEMi : {} {} {} {} {} {}", newSalary.multiply(BigDecimal.valueOf(tenure)), acBalance, creditSum, debitSum, balanceCushion, oldSalary);

            BigDecimal oldBalanceCushion = oldSalary.add(creditSum).subtract(debitSum);
            BigDecimal oldEmiFactor = oldEmi.divide(oldBalanceCushion, SCALE, ROUNDING);

            BigDecimal newFactor = newSalary.add(creditSum).subtract(debitSum);
            BigDecimal newEmi = newFactor.multiply(oldEmiFactor).setScale(SCALE, ROUNDING);

            // Balance Cushion calculations
            BigDecimal numerator = newEmi;
            BigDecimal denominator = newEmi.subtract(lastPrincipal.multiply(interestRate));
            BigDecimal ratio = numerator.divide(denominator, SCALE, ROUNDING);

            logger.debug("Ratio : {}", ratio);
            if (ratio.compareTo(BigDecimal.ZERO) <= 0 ){
                throw new InvalidInputException("Invalid EMI Calculation : EMI is less than the projected interest amount.");
            }

            double logRatio = Math.log(ratio.doubleValue());
            double logOnePlusR = Math.log(1 + interestRate.doubleValue());

            int n = (int) Math.ceil(logRatio / logOnePlusR);

            logger.debug("New Tenure : {}",n);

            BigDecimal surplusOfBal = acBalance.subtract(balanceCushion);
            BigDecimal surPerEmi = surplusOfBal.divide(BigDecimal.valueOf(n), SCALE, ROUNDING);

            BigDecimal tempSugg = newEmi.add(surPerEmi);

            List<BigDecimal> resSuggested = new ArrayList<>();
            resSuggested.add(debitSum.divide(BigDecimal.valueOf(tenure), SCALE, ROUNDING));
            resSuggested.add(tempSugg.setScale(2, ROUNDING));
            resSuggested.add(newSalary.add(creditSum.divide(BigDecimal.valueOf(tenure), SCALE, ROUNDING)).subtract(debitSum.divide(BigDecimal.valueOf(tenure), SCALE, ROUNDING)));
            return resSuggested;
        } catch (Exception ex) {
            logger.error("Error in suggestEmiIncrease: newSalary={}, acBalance={}, tenure={}", newSalary, acBalance, tenure, ex);
            throw ex;
        }
    }

    public LoanAmortizationResponse getAmortizedEmi(BigDecimal suggestedEmi, List<RepaymentEvent> eventList, Integer installmentPaid, double tenure){
        if(installmentPaid == null){
            for(int i=0;i<eventList.size();i++){
                if(eventList.get(i).getPaidDt() == null){
                    installmentPaid=i+1;
                    break;
                }
            }
        }
        try {
            BigDecimal interestRate = eventList.get(1).getProjectedIntAmt().divide(eventList.get(1).getExpectedPrincipal(), SCALE, ROUNDING);
            BigDecimal lastPrincipal = eventList.get(installmentPaid-1).getExpectedPrincipal();

            List<List<BigDecimal>> principalList = new ArrayList<>();
            while(lastPrincipal.compareTo(suggestedEmi) > 0){
                logger.debug("Installment calculation: principal={}, interestRate={}, suggestedEmi={}", lastPrincipal, interestRate, suggestedEmi);
                //lastPrincipal-=suggestedEmi-lastPrincipal.multiply(interestRate);
                lastPrincipal = lastPrincipal.subtract(suggestedEmi.subtract(lastPrincipal.multiply(interestRate))).setScale(SCALE, ROUNDING);
                List<BigDecimal> tempInstallment = new ArrayList<>();
                tempInstallment.add(suggestedEmi);
                //tempInstallment.add(Math.round((suggestedEmi - lastPrincipal*interestRate)*100.0)/100.0);
                tempInstallment.add(suggestedEmi.subtract(lastPrincipal.multiply(interestRate)).setScale(2, RoundingMode.HALF_EVEN));
                tempInstallment.add(lastPrincipal);
                //tempInstallment.add(Math.round((lastPrincipal*interestRate)*100.0)/100.0);
                tempInstallment.add((lastPrincipal.multiply(interestRate)).setScale(2, RoundingMode.HALF_EVEN));
                principalList.add(tempInstallment);
            }
            List<BigDecimal> tempInstallment = new ArrayList<>();

            //tempInstallment.add(lastPrincipal/(1-interestRate));
            //tempInstallment.add(lastPrincipal);
            //tempInstallment.add(0.0);
            //tempInstallment.add(Math.round((lastPrincipal*interestRate)*100.0)/100.0);

            tempInstallment.add(lastPrincipal.divide(BigDecimal.ONE.subtract(interestRate), SCALE, ROUNDING));
            tempInstallment.add(lastPrincipal);
            tempInstallment.add(BigDecimal.ZERO);
            tempInstallment.add(lastPrincipal.multiply(interestRate).setScale(2, RoundingMode.HALF_UP));

            principalList.add(tempInstallment);

            BigDecimal oldAmortizedValue= new BigDecimal("0.0");
            BigDecimal newAmortizedValue = new BigDecimal("0.0");
            for(int i=0;i<eventList.size();i++){
                oldAmortizedValue=oldAmortizedValue.add(eventList.get(i).getInstallmentAmt());
                if(i<installmentPaid)   newAmortizedValue=newAmortizedValue.add(eventList.get(i).getInstallmentAmt());
            }
            for(int i=0; i<principalList.size(); i++){
                newAmortizedValue=newAmortizedValue.add(principalList.get(i).get(0));
            }
            logger.debug("NEW vs OLD Value : {} {}", newAmortizedValue, oldAmortizedValue);

            LoanAmortizationResponse loanRes = new LoanAmortizationResponse();
            loanRes.setCurrentEmi(eventList.get(0).getInstallmentAmt());
            loanRes.setSuggestedEmi(suggestedEmi);
            loanRes.setCurrentAmortizedValue(oldAmortizedValue);
            loanRes.setNewAmortizedValue(newAmortizedValue.setScale(2, RoundingMode.HALF_UP));
            loanRes.setOutstandingPrincipal(eventList.get(installmentPaid).getExpectedPrincipal());
            loanRes.setCurrentNoOfInstallments(Long.valueOf(eventList.size()));
            loanRes.setNewNoOfInstallments(Long.valueOf(installmentPaid+principalList.size()));
            loanRes.setSavingsOnInterest(oldAmortizedValue.subtract(newAmortizedValue).setScale(2, RoundingMode.HALF_UP));

            List<AmortizedSchedule> resAmortizedSchedule = new ArrayList<>();
            for(int i=0; i<installmentPaid+principalList.size(); i++){
                AmortizedSchedule temp = new AmortizedSchedule();
                temp.setLoanId(eventList.get(0).getLoanId());
                if(i<installmentPaid){
                    temp.setInstallmentAmount(eventList.get(i).getInstallmentAmt());
                    temp.setPrincipalAmount(eventList.get(i).getPrincipalAmt());
                    temp.setExpectedPrincipal(eventList.get(i).getExpectedPrincipal());
                    temp.setProjectedInterestAmount(eventList.get(i).getProjectedIntAmt());
                    temp.setDueDate(eventList.get(i).getDueDt());
                } else {
                    temp.setInstallmentAmount(principalList.get(i-installmentPaid).get(0).setScale(2, RoundingMode.HALF_UP));
                    temp.setPrincipalAmount(principalList.get(i-installmentPaid).get(1).setScale(2, RoundingMode.HALF_UP));
                    temp.setExpectedPrincipal(principalList.get(i-installmentPaid).get(2).setScale(2, RoundingMode.HALF_UP));
                    temp.setProjectedInterestAmount(principalList.get(i-installmentPaid).get(3));
                    if(i>eventList.size()-1){
                        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyyMMdd");
                        LocalDate d1 = LocalDate.parse(resAmortizedSchedule.get(i-1).getDueDate(), f).plusMonths((long) tenure);
                        temp.setDueDate(d1.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    } else temp.setDueDate(eventList.get(i).getDueDt());
                }
                resAmortizedSchedule.add(temp);
            }
            loanRes.setNewSchedule(resAmortizedSchedule);

            return loanRes;
        } catch (Exception ex) {
            logger.error("Error in getAmortizedEmi: suggestedEmi={}, eventList.size={}, installmentPaid={}", suggestedEmi, eventList != null ? eventList.size() : null, installmentPaid, ex);
            throw ex;
        }
    }











     // New Logic to calculate the suggested emi
     public List<BigDecimal> suggestEmiIncreaseTwo(BigDecimal newSalary, BigDecimal accountBalance, List<TransactionHistory> listHist, BigDecimal balanceCushion, long tenure, BigDecimal oldSalary, BigDecimal oldEmi) {

         BigDecimal totalDebit = BigDecimal.ZERO;

         for (TransactionHistory tx : listHist) {
             if (tx.getDebitCreditFlag() == 2) { // Debit
                 totalDebit = totalDebit.add(tx.getMovementAmount());
             }
         }

         BigDecimal months = BigDecimal.valueOf(tenure);

         BigDecimal avgMonthlyExpense =
                 totalDebit.divide(months, 2, RoundingMode.HALF_UP);

         BigDecimal disposableIncome =
                 newSalary.subtract(avgMonthlyExpense);

         if (disposableIncome.compareTo(BigDecimal.ZERO) <= 0) {
             throw new InvalidInputException("No disposable income available for EMI");
         }

         // FOIR limit – 40%
         BigDecimal maxAffordableEmi =
                 newSalary.multiply(new BigDecimal("0.50"))
                         .setScale(2, RoundingMode.HALF_UP);

         // Liquidity safety
         BigDecimal usableBalance =
                 accountBalance.subtract(balanceCushion);

         if (usableBalance.compareTo(BigDecimal.ZERO) <= 0) {
             throw new InvalidInputException("Insufficient balance after cushion");
         }

         BigDecimal liquidityEmiCap =
                 usableBalance.divide(months, 2, RoundingMode.HALF_UP);

         BigDecimal suggestedEmi;

         // Salary decrease handling
         if (newSalary.compareTo(oldSalary) < 0) {
             suggestedEmi = oldEmi.min(maxAffordableEmi);
         } else {
             BigDecimal growthAllowance =
                     disposableIncome.subtract(oldEmi)
                             .multiply(new BigDecimal("0.50"));

             suggestedEmi = oldEmi.add(growthAllowance);
         }

         suggestedEmi = suggestedEmi
                 .min(maxAffordableEmi)
                 .min(disposableIncome)
                 //.min(liquidityEmiCap)
                 .setScale(2, RoundingMode.HALF_UP);

         List<BigDecimal> result = new ArrayList<>();
         result.add(avgMonthlyExpense);     // index 0
         result.add(suggestedEmi);          // index 1
         result.add(disposableIncome);      // index 2

         return result;
     }

    public List<BigDecimal> suggestEmiIncreaseThree(
            BigDecimal newSalary,
            BigDecimal acBalance,
            List<TransactionHistory> listHist,
            BigDecimal balanceCushion,
            long tenure,
            BigDecimal oldSalary,
            BigDecimal oldEmi) {

        // 1️⃣ Calculate average monthly credits & debits
        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;
        int months = Math.max(1, (int) tenure);

        for (TransactionHistory tx : listHist) {
            if (tx.getDebitCreditFlag() == 1) {
                totalCredit = totalCredit.add(tx.getMovementAmount());
            } else if (tx.getDebitCreditFlag() == 2) {
                totalDebit = totalDebit.add(tx.getMovementAmount());
            }
        }

        logger.debug("Inside suggestedEMi : {} {} {} {} {} {}", newSalary.multiply(BigDecimal.valueOf(tenure)), acBalance, totalCredit, totalDebit, balanceCushion, oldSalary);

        BigDecimal avgMonthlyCredit =
                totalCredit.divide(BigDecimal.valueOf(months), SCALE, ROUNDING);

        BigDecimal avgMonthlyDebit =
                totalDebit.divide(BigDecimal.valueOf(months), SCALE, ROUNDING);

        // 2️⃣ Monthly surplus
        BigDecimal monthlySurplus =
                newSalary.add(avgMonthlyCredit).subtract(avgMonthlyDebit);

        if (monthlySurplus.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidInputException("No monthly surplus available to pay EMI");
        }

        // 3️⃣ EMI affordability limits
        BigDecimal maxEmiByIncome =
                newSalary.multiply(new BigDecimal("0.50")); // 40% FOIR

        BigDecimal maxEmiBySurplus =
                monthlySurplus.subtract(balanceCushion);

        if (maxEmiBySurplus.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidInputException(
                    "Monthly surplus insufficient after maintaining balance cushion"
            );
        }

        // 4️⃣ Salary-based EMI adjustment
        BigDecimal salaryFactor =
                newSalary.divide(oldSalary, SCALE, ROUNDING);

        BigDecimal rawSuggestedEmi =
                oldEmi.multiply(salaryFactor);

        // 5️⃣ FINAL SAFE EMI
        BigDecimal finalSuggestedEmi =
                rawSuggestedEmi
                        .min(maxEmiByIncome)
                        .min(maxEmiBySurplus)
                        .setScale(2, ROUNDING);

        // 6️⃣ Result
        List<BigDecimal> result = new ArrayList<>();
        result.add(avgMonthlyDebit);     // monthly expenses
        result.add(finalSuggestedEmi);   // suggested EMI
        result.add(monthlySurplus);      // savings

        return result;
    }



}