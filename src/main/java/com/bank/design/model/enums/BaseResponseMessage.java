package com.bank.design.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BaseResponseMessage {
    SUCCESSFUL("successful"),
    FAILED("failed");

    private final String value;
}
