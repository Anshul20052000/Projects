// TransactionHistoryResponse.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.thinkapi.loan_amortisation.dto.transactionHistory;
import java.util.List;
import lombok.Data;

@Data
public class TransactionHistoryResponse {
    private Long noOfPages;
    private Long totalNoOfRecords;
    private Long pageSize;
    private String hasNext;
    private List<TransactionHistory> transactionHistory;
    private Long pageNum;
}

// TransactionHistory.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

