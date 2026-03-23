package com.bank.design.model.dto.request;

import jakarta.validation.constraints.DecimalMax;
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
public class DepositRequest {
    @NotBlank(message = "account Number is required")
    private String accountNumber;

    @NotNull(message = "amount is required.")
    @DecimalMin(value = "1.00", inclusive = true, message = "Amount must be between ₦1.00 and ₦1,000,000.00.")
    @DecimalMax(value = "1000000.00", inclusive = true, message = "Amount must be between ₦1.00 and ₦1,000,000.00.")
    private BigDecimal amount;
}
