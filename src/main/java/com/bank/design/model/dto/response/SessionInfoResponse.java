package com.bank.design.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionInfoResponse {
    private String username;

}