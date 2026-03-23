package com.bank.design.model.dto.response;

import com.bank.design.model.enums.BaseResponseMessage;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private int responseCode;

    private String message;

    private boolean success;

    private T data;

    public static <T> BaseResponse<T> success(final T data){
        return BaseResponse.<T>builder()
                .responseCode(200)
                .data(data)
                .success(true)
                .message(BaseResponseMessage.SUCCESSFUL.getValue())
                .build();
    }
    public static <T> BaseResponse<T> success(){
        return BaseResponse.<T>builder()
                .responseCode(200)
                .success(true)
                .message(BaseResponseMessage.SUCCESSFUL.getValue())
                .build();
    }

    public static <T> BaseResponse<T> error(int errorCode){
        return BaseResponse.<T>builder()
                .responseCode(errorCode)
                .success(false)
                .message(BaseResponseMessage.FAILED.getValue())
                .build();
    }

    public static <T> BaseResponse<T> error(int errorCode, String errorMessage){
        return BaseResponse.<T>builder()
                .responseCode(errorCode)
                .success(false)
                .message(errorMessage)
                .build();
    }

}
