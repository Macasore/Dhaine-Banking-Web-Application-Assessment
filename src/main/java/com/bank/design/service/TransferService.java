package com.bank.design.service;

import com.bank.design.model.dto.request.CreateAccountRequest;
import com.bank.design.model.dto.request.DepositRequest;
import com.bank.design.model.dto.request.LoginRequest;
import com.bank.design.model.dto.request.WithdrawalRequest;
import com.bank.design.model.dto.response.BaseResponse;
import com.bank.design.model.dto.response.LoginResponse;

import java.io.IOException;

public interface TransferService {
    BaseResponse<String> deposit(DepositRequest request);

    BaseResponse<String> withdraw(WithdrawalRequest request);
}
