package com.bank.design.model.dto.response;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private boolean success;

    private String accessToken;
}
