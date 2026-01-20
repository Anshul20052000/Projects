package com.thinkapi.loan_amortisation.service.api.transactionHistory;

import com.thinkapi.loan_amortisation.dto.fallbackParser.FallBackParser;
import com.thinkapi.loan_amortisation.dto.transactionHistory.TransactionHistoryRequest;
import com.thinkapi.loan_amortisation.dto.transactionHistory.TransactionHistoryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransactionHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionHistoryService.class);
    private final WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public TransactionHistoryService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.tcsbancs.com/external/BaNCS/Banking/AccountManagement/v2/v1/accountManagement/account").build();
    }

    public Mono<TransactionHistoryResponse> getTransactionHistory(TransactionHistoryRequest req) {
        logger.debug("Calling getTransactionHistory with request: {}", req);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/transactionHistory/{accountReference}")
                        .queryParam("fromDate", req.getFromDate())
                        .queryParam("customerId", req.getCustomerId())
                        .queryParam("toDate", req.getToDate())
                        .queryParam("fromAmount", req.getFromAmount())
                        .queryParam("toAmount", req.getToAmount())
                        .queryParam("transactionType", req.getTransactionType())
                        .queryParam("transactionAccountType", req.getTransactionAccountType())
                        .queryParam("transactionId", req.getTransactionId())
                        .queryParam("movementDescription", req.getMovementDescription())
                        .queryParam("extensibilityMap", req.getExtensibilityMap())
                        .queryParam("pageSize", req.getPageSize())
                        .queryParam("pageNum", req.getPageNum())
                        .build(req.getAccountReference()))
                // headers
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
                .bodyToMono(TransactionHistoryResponse.class)
                .doOnNext(response -> logger.debug("TransactionHistoryResponse received: {}", response))
                .onErrorResume(ex -> {
                    logger.error("TransactionHistory API ERROR → Using fallback JSON", ex);
                    FallBackParser fbp = new FallBackParser();
                    String fallbackJson = fbp.getFallbackJson("fallback/transactionHistory.json");

                    try {
                        TransactionHistoryResponse fallbackResponse = objectMapper.readValue(fallbackJson, TransactionHistoryResponse.class);
                        logger.debug("Fallback TransactionHistoryResponse used: {}", fallbackResponse);
                        return Mono.just(fallbackResponse);
                    } catch (Exception e) {
                        logger.error("Failed to parse fallback JSON", e);
                        return Mono.error(new RuntimeException("Failed to parse fallback JSON", e));
                    }
                });
    }
}

