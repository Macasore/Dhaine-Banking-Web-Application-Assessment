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
public class WithdrawalRequest {
    @NotBlank(message = "account Number is required")
    private String accountNumber;

    @NotBlank(message = "password is required")
    private String password;

    @NotNull(message = "amount is required.")
    @DecimalMin(value = "1.00", inclusive = true, message = "Amount must be at least ₦1.00 .")
    private BigDecimal amount;

    private String narration;
}
