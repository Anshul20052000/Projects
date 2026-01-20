package com.thinkapi.loan_amortisation.service.api.loanRepayment;

import com.thinkapi.loan_amortisation.dto.fallbackParser.FallBackParser;
import com.thinkapi.loan_amortisation.dto.loan.LoanRepaymentRequest;
import com.thinkapi.loan_amortisation.dto.loan.LoanRepaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LoanRepaymentService {

    private static final Logger logger = LoggerFactory.getLogger(LoanRepaymentService.class);
    private final WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public LoanRepaymentService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.tcsbancs.com/external/bancs/GBP/loans/V2/v1/loans").build();
    }

    public Mono<LoanRepaymentResponse> getLoanRepayment(LoanRepaymentRequest req) {
        logger.debug("Calling getLoanRepayment with request: {}", req);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/repaymentSchedule/{loanReference}")
                        .queryParam("repayOption", req.getRepayOption())
                        .queryParam("pageSize", req.getPageSize())
                        .queryParam("pageNum", req.getPageNum())
                        .build(req.getLoanReference()))
                // Headers
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
                .bodyToMono(LoanRepaymentResponse.class)
                .doOnNext(response -> logger.debug("LoanRepaymentResponse received: {}", response))
                .onErrorResume(ex -> {
                    logger.error("LoanRepayment API ERROR → Using fallback JSON", ex);
                    FallBackParser fbp = new FallBackParser();
                    String fallbackJson = fbp.getFallbackJson("fallback/repaymentFallback.json");
                    try {
                        LoanRepaymentResponse fallbackResponse = objectMapper.readValue(fallbackJson, LoanRepaymentResponse.class);
                        logger.debug("Fallback LoanRepaymentResponse used: {}", fallbackResponse);
                        return Mono.just(fallbackResponse);
                    } catch (Exception e) {
                        logger.error("Failed to parse fallback JSON", e);
                        return Mono.error(new RuntimeException("Failed to parse fallback JSON", e));
                    }
                });
    }
}

