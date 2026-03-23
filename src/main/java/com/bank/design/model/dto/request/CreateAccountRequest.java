package com.bank.design.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    @DecimalMin(value = "500.00", inclusive = true, message = "initial deposit must be at least 500")
    private Double initialDeposit;
}
