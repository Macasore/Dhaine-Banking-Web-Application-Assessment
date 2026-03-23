package com.bank.design.errorhandling;

import com.bank.design.errorhandling.exceptions.GenericErrorCodeException;
import com.bank.design.model.dto.response.BaseErrorResponse;
import com.bank.design.model.enums.BaseResponseStatus;
import com.bank.design.model.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {

    private static String EXCEPTION_WAS_THROWN = "Exception was thrown: ";


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error(EXCEPTION_WAS_THROWN, ex);
        final List<FieldError> fieldErrors = ex.getFieldErrors();
        final String[] errors = new String[fieldErrors.size()];

        for (int i = 0; i < fieldErrors.size(); i++) {
            final FieldError fieldError = fieldErrors.get(i);
            if (fieldError.getRejectedValue() != null && fieldError.getRejectedValue().getClass().isEnum()) {
                errors[i] = String.format("The value provided for '%s' is invalid.", fieldError.getField());
            } else {
                errors[i] = String.format("%s", fieldError.getDefaultMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapErrorCode(ErrorCode.BAD_REQUEST, errors));
    }

    @ExceptionHandler(GenericErrorCodeException.class)
    public ResponseEntity<BaseErrorResponse> handleGenericErrorCodeException(GenericErrorCodeException ex){
        log.error(EXCEPTION_WAS_THROWN, ex);

        if (!ex.getMessage().isEmpty()){
            return ResponseEntity.status(ex.getHttpStatus())
                    .body(mapErrorCode(ex.getErrorCode(), ex.getMessage()));
        }

        return ResponseEntity.status(ex.getHttpStatus())
                .body(mapErrors(ex.getErrorCode()));

    }

    private BaseErrorResponse mapErrorCode(ErrorCode errorCode, String... message){
        return BaseErrorResponse.error(new ErrorCode[]{errorCode}, message);
    }

    private BaseErrorResponse mapErrors(ErrorCode... errorCodes){
        return BaseErrorResponse.error(errorCodes, Arrays.stream(errorCodes).map(ErrorCode::getDescription).toList().toArray(String[]::new));

    }
}
