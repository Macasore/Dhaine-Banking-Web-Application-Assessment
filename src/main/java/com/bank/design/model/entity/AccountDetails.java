package com.bank.design.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class AccountDetails{
    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("initialDeposit")
    private BigDecimal initialDeposit;
    }