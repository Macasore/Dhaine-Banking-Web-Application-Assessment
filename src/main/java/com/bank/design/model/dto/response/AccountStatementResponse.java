package com.bank.design.model.dto.response;

import com.bank.design.model.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatementResponse {

    @JsonProperty("transactionDate")
    private String transactionDate;

    @JsonProperty("transactionType")
    private TransactionType transactionType;

    @JsonProperty("narration")
    private String narration;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("accountBalance")
    private BigDecimal accountBalance;
}
