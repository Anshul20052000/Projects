package com.thinkapi.loan_amortisation.service.api.accountBalance;

import com.thinkapi.loan_amortisation.dto.accountDetails.AccountBalanceRequest;
import com.thinkapi.loan_amortisation.dto.accountDetails.AccountBalanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AccountBalanceService {

    private static final Logger logger = LoggerFactory.getLogger(AccountBalanceService.class);
    private final WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public AccountBalanceService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.tcsbancs.com/external/BaNCS/Banking/AccountManagement/v2/v1/accountManagement/account").build();
    }

    public Mono<AccountBalanceResponse> getAccountBalance(AccountBalanceRequest req) {
        logger.debug("Calling getAccountBalance with request: {}", req);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/balanceDetails/{accountReference}")
                        .build(req.getAccountReference()))
                .header("userId", String.valueOf(req.getUserId()))
                .header("entity", req.getEntity())
                .header("languageCode", String.valueOf(req.getLanguageCode()))
                .header("ChannelType", String.valueOf(req.getChannelType()))
                .header("Co-Relationid", String.valueOf(req.getCoRelationId()))
                .header("UUIDSeqNo", String.valueOf(req.getUuidSeqNo()))
                .header("InitiatingSystem", req.getInitiatingSystem())
                .header("ServiceMode", String.valueOf(req.getServiceMode()))
                .header("Accesstoken", req.getAccessToken())
                .header("referenceId", req.getReferenceId())
                .retrieve()
                .bodyToMono(AccountBalanceResponse.class)
                .doOnNext(response -> logger.debug("AccountBalanceResponse received: {}", response))
                .onErrorResume(ex -> {
                    logger.error("Account API ERROR → Using fallback JSON", ex);
                    String fallbackJson = """
                            {
                              "pageSize": 22,
                              "pageNum": 1,
                              "noOfPages": 1,
                              "totalNoOfRecords": 1,
                              "hasNext": "N",
                              "accountBalanceDetails": {
                                "balance": {
                                  "creditLine": {
                                    "amount": {
                                      "creditAmount": 0,
                                      "currency": "USD"
                                    },
                                    "included": "true",
                                    "balanceType": "Available"
                                  },
                                  "amount": {
                                    "accountBalance": 16491.24,
                                    "balanceAmountCurrency": "USD"
                                  },
                                  "accountReference": "999100200107374",
                                  "dateTime": "20251208",
                                  "type": "Information",
                                  "creditDebitIndicator": "Credit"
                                },
                                "extensibilityMap": null
                              }
                            }
                    """;
                    try {
                        AccountBalanceResponse fallbackResponse = objectMapper.readValue(fallbackJson, AccountBalanceResponse.class);
                        logger.debug("Fallback AccountBalanceResponse used: {}", fallbackResponse);
                        return Mono.just(fallbackResponse);
                    } catch (Exception e) {
                        logger.error("Failed to parse fallback JSON", e);
                        return Mono.error(new RuntimeException("Failed to parse fallback JSON", e));
                    }
                });
    }
}