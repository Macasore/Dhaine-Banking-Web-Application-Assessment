package com.bank.design.model.dto.response;

import com.bank.design.model.enums.BaseResponseStatus;
import com.bank.design.model.enums.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BaseErrorResponse {

    private BaseResponseStatus status;
    private ErrorCode[] errors;
    private String[] messages;

    public static BaseErrorResponse error(final ErrorCode[] errorCodes, final String[] errorMessages) {
        return BaseErrorResponse.builder()
                .errors(errorCodes)
                .messages(errorMessages)
                .status(BaseResponseStatus.ERROR)
                .build();
    }

    public static BaseErrorResponse error(final ErrorCode errorCode, final String errorMessage) {
        return BaseErrorResponse.builder()
                .errors(new ErrorCode[] {errorCode})
                .messages(new String[] {errorMessage})
                .status(BaseResponseStatus.ERROR)
                .build();
    }

    public static BaseErrorResponse error(final ErrorCode errorCode) {
        return error(errorCode, errorCode.getDescription());
    }
}
