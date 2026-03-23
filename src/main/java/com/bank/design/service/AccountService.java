package com.bank.design.service;

import com.bank.design.model.dto.response.AccountInfoResponse;
import com.bank.design.model.dto.response.AccountStatementResponse;
import com.bank.design.model.dto.response.BaseResponse;

import java.util.List;

public interface AccountService {
    BaseResponse<AccountInfoResponse> getAccountInfo(String accountNumber);

    BaseResponse<List<AccountStatementResponse>> getAccountStatement(String accountNumber);
}
