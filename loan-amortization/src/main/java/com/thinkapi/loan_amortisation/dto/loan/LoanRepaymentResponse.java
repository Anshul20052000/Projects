// LoanRepaymentResponse.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.thinkapi.loan_amortisation.dto.loan;
import java.util.List;
import lombok.Data;

@Data
public class LoanRepaymentResponse {
    private Long noOfPages;
    private LoanDetails loanDetails;
    private Long totalNoOfRecords;
    private Long pageSize;
    private String hasNext;
    private List<RepaymentEvent> repaymentEvents;
    private Long pageNum;
}

// LoanDetails.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

// RepaymentEvent.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

