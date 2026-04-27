package com.bank.design.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {

    @NotBlank(message = "account name is required")
    private String accountName;

    @NotBlank(message = "account password is required")
    private String accountPassword;

    @NotBlank
    private String confirmPassword;

    @NotNull(message = "Initial deposit required")
    private BigDecimal initialDeposit;
}
