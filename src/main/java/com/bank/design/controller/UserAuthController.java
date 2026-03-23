package com.bank.design.controller;

import com.bank.design.model.dto.request.CreateAccountRequest;
import com.bank.design.model.dto.request.LoginRequest;
import com.bank.design.model.dto.response.BaseResponse;
import com.bank.design.model.dto.response.LoginResponse;
import com.bank.design.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;


    @PostMapping("/create_account")
    public ResponseEntity<BaseResponse<String>> createAccount(@RequestBody @Valid CreateAccountRequest request){
        var response = userAuthService.createUserAccount(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) throws IOException {
        var response = userAuthService.login(request);

        return ResponseEntity.ok(response);

    }
}
