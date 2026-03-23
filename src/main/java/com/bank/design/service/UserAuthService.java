package com.bank.design.service;

import com.bank.design.model.dto.request.CreateAccountRequest;
import com.bank.design.model.dto.request.LoginRequest;
import com.bank.design.model.dto.response.BaseResponse;
import com.bank.design.model.dto.response.LoginResponse;

import java.io.IOException;

public interface UserAuthService {

    BaseResponse<String> createUserAccount(CreateAccountRequest createAccountRequest);

    BaseResponse<LoginResponse> login(LoginRequest request) throws IOException;
}
