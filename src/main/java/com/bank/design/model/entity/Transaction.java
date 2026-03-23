package com.bank.design.model.entity;

import com.bank.design.model.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("previousBalance")
    private BigDecimal previousBalance;

    @JsonProperty("newBalance")
    private BigDecimal newBalance;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("transactionType")
    private TransactionType transactionType;

    @JsonProperty("createdAt")
    private String createdAt;
}
