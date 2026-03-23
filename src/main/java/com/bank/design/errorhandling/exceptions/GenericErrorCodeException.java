package com.bank.design.errorhandling.exceptions;

import com.bank.design.model.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@RequiredArgsConstructor
public class GenericErrorCodeException extends RuntimeException{
    private String message;
    private ErrorCode errorCode;
    private HttpStatus httpStatus;

    public GenericErrorCodeException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
