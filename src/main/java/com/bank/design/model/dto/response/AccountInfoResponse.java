package com.bank.design.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoResponse {

    @JsonProperty("accountName")
    private String accountName;

    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("accountBalance")
    private BigDecimal accountBalance;
}
