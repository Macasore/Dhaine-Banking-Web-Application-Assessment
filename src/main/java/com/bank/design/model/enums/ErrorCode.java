package com.bank.design.model.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR("500", "An error occurred while trying to process your request, try again!"),
    UNAUTHORIZED("401", "Unauthorized"),
    BAD_REQUEST("400", "Bad request"),
    NOT_FOUND("404", "Resource not found")
    ;

    private final String code;
    private final String description;

    ErrorCode(final String code, final String description) {
        this.code = code;
        this.description = description;
    }
}
