package com.bank.design.errorhandling;

import com.bank.design.errorhandling.exceptions.GenericErrorCodeException;
import com.bank.design.model.dto.response.BaseResponse;
import com.bank.design.model.dto.response.ErrorDetailResponse;
import com.bank.design.model.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@Component
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {

    private static String EXCEPTION_WAS_THROWN = "Exception was thrown: ";
    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed";


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<List<ErrorDetailResponse>>> handleValidationException(MethodArgumentNotValidException ex) {
        log.error(EXCEPTION_WAS_THROWN, ex);
        final List<FieldError> fieldErrors = ex.getFieldErrors();
        final List<ErrorDetailResponse> errorDetails = fieldErrors.stream()
                .map(this::toValidationErrorDetail)
                .toList();

        return errorResponse(
                HttpStatus.BAD_REQUEST,
                VALIDATION_FAILED_MESSAGE,
                errorDetails
        );
    }

    @ExceptionHandler(GenericErrorCodeException.class)
    public ResponseEntity<BaseResponse<List<ErrorDetailResponse>>> handleGenericErrorCodeException(GenericErrorCodeException ex){
        log.error(EXCEPTION_WAS_THROWN, ex);
        final ErrorCode errorCode = ex.getErrorCode();
        final String errorMessage = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : errorCode.getDescription();

        return errorResponse(
                ex.getHttpStatus(),
                errorMessage,
                List.of(toErrorDetail(errorCode, errorMessage, null))
        );

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<List<ErrorDetailResponse>>> handleUnexpectedException(Exception ex) {
        log.error(EXCEPTION_WAS_THROWN, ex);
        return errorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_SERVER_ERROR.getDescription(),
                List.of(toErrorDetail(ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getDescription(), null))
        );
    }

    private ErrorDetailResponse toValidationErrorDetail(FieldError fieldError) {
        final String errorMessage;
        if (fieldError.getRejectedValue() != null && fieldError.getRejectedValue().getClass().isEnum()) {
            errorMessage = String.format("The value provided for '%s' is invalid.", fieldError.getField());
        } else {
            errorMessage = String.valueOf(fieldError.getDefaultMessage());
        }

        return toErrorDetail(ErrorCode.BAD_REQUEST, errorMessage, fieldError.getField());
    }

    private ErrorDetailResponse toErrorDetail(ErrorCode errorCode, String errorMessage, String field) {
        return ErrorDetailResponse.builder()
                .code(errorCode.getCode())
                .message(errorMessage)
                .field(field)
                .build();
    }

    private ResponseEntity<BaseResponse<List<ErrorDetailResponse>>> errorResponse(
            HttpStatus httpStatus,
            String message,
            List<ErrorDetailResponse> errorDetails
    ) {
        return ResponseEntity.status(httpStatus)
                .body(BaseResponse.error(httpStatus.value(), message, errorDetails));
    }
}
