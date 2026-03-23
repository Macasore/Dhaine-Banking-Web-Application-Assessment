package com.bank.design.controller;

import com.bank.design.model.dto.request.DepositRequest;
import com.bank.design.model.dto.request.WithdrawalRequest;
import com.bank.design.model.dto.response.BaseResponse;
import com.bank.design.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;


    @PostMapping("/deposit")
    public ResponseEntity<BaseResponse<String>> deposit(@RequestBody @Valid DepositRequest depositRequest){
        var response = transferService.deposit(depositRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<BaseResponse<String>> withdraw(@RequestBody @Valid WithdrawalRequest withdrawalRequest){
        var response = transferService.withdraw(withdrawalRequest);
        return ResponseEntity.ok(response);
    }
}
