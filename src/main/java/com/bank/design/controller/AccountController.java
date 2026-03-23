package com.bank.design.controller;

import com.bank.design.model.dto.response.AccountInfoResponse;
import com.bank.design.model.dto.response.AccountStatementResponse;
import com.bank.design.model.dto.response.BaseResponse;
import com.bank.design.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/account_info/{accountNumber}")
    public ResponseEntity<BaseResponse<AccountInfoResponse>> getAccountInfo(@PathVariable String accountNumber) {
        var response = accountService.getAccountInfo(accountNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account_statement/{accountNumber}")
    public ResponseEntity<BaseResponse<List<AccountStatementResponse>>> getAccountStatement(@PathVariable String accountNumber) {
        var response = accountService.getAccountStatement(accountNumber);
        return ResponseEntity.ok(response);
    }
}
