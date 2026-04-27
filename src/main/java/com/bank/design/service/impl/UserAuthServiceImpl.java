package com.bank.design.service.impl;

import com.bank.design.model.dto.request.CreateAccountRequest;
import com.bank.design.model.dto.request.LoginRequest;
import com.bank.design.model.dto.response.BaseResponse;
import com.bank.design.model.dto.response.LoginResponse;
import com.bank.design.model.entity.AccountDetails;
import com.bank.design.model.entity.Transaction;
import com.bank.design.model.entity.User;
import com.bank.design.model.enums.ErrorCode;
import com.bank.design.errorhandling.exceptions.GenericErrorCodeException;
import com.bank.design.model.enums.TransactionType;
import com.bank.design.service.UserAuthService;
import com.bank.design.utils.DateUtil;
import com.bank.design.utils.JsonUtil;
import com.bank.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {
    private final JsonUtil jsonUtil;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public BaseResponse<String> createUserAccount(CreateAccountRequest createAccountRequest) {
        try{
           checkUserExistence(createAccountRequest.getAccountName());

           User userObject = createUserObject(createAccountRequest);
           jsonUtil.createUserObject(userObject);
            if (createAccountRequest.getInitialDeposit() > 0){
                Transaction newTransaction = Transaction.builder()
                        .accountNumber(userObject.getAccountDetails().getAccountNumber())
                        .amount(BigDecimal.valueOf(createAccountRequest.getInitialDeposit()))
                        .newBalance(BigDecimal.valueOf(createAccountRequest.getInitialDeposit()))
                        .previousBalance(BigDecimal.ZERO)
                        .narration("Initial Deposit")
                        .transactionType(TransactionType.CREDIT)
                        .createdAt(DateUtil.formatToString(LocalDateTime.now()))
                        .build();

                jsonUtil.addTransaction(newTransaction);
            }
            return BaseResponse.success(userObject.getAccountDetails().getAccountNumber());
        } catch (GenericErrorCodeException ex){
            throw ex;
        }
        catch (Exception ex){
            throw new GenericErrorCodeException("An error occurred", ErrorCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private User createUserObject(CreateAccountRequest createAccountRequest) throws IOException {
        User newUser = new User(createAccountRequest.getAccountName(), createAccountRequest.getAccountPassword());

        String accountNumber = createAccountNumber();
        AccountDetails accountDetails = new AccountDetails(accountNumber, createAccountRequest.getInitialDeposit());

        newUser.addAccountDetails(accountDetails);
        return newUser;
    }

    @Override
    public BaseResponse<LoginResponse> login(LoginRequest request) throws IOException {
        try{
            if (request.getAccountNumber().length() != 10){
                throw new GenericErrorCodeException("Account number must be 10 digits", ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST);
            }
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getAccountNumber(), request.getAccountPassword()));

            User user = new User(request.getAccountNumber(), "");

            String token = jwtUtil.createToken(user);

            LoginResponse loginResponse = LoginResponse.builder()
                    .accessToken(token)
                    .success(true)
                    .build();

            log.info("Logged in successful");
            return BaseResponse.success(loginResponse);
        } catch (BadCredentialsException e){
            throw new GenericErrorCodeException("Invalid username or password", ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        } catch (GenericErrorCodeException e) {
            throw e;
        }
        catch (Exception e){
            throw new GenericErrorCodeException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void checkUserExistence(String accountName) throws IOException {
        List<User> users = jsonUtil.returnUsers().stream()
                .filter(user -> Objects.equals(user.getAccountName(), accountName))
                .toList();

        if (!users.isEmpty()){
            throw new GenericErrorCodeException("User already exists", ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }
    }

    private String createAccountNumber() throws IOException {
        Random rand = new Random();
        String accountNumber = "";
        boolean exists = true;
        int attempts = 0;

        while (exists && attempts < 20) {

            long number = (long) (rand.nextDouble() * 10000000000L);
            accountNumber = String.format("%010d", number);

            exists = checkAccountNumberExistence(accountNumber);
            attempts++;
        }

        if (exists) {
            throw new GenericErrorCodeException("Could not generate a unique account number after multiple attempts. Please try again", ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }

        return accountNumber;
    }

    private boolean checkAccountNumberExistence(String accountNumber) throws IOException {
        return jsonUtil.returnUsers().stream()
                .anyMatch(user -> user.getAccountDetails() != null && Objects.equals(user.getAccountDetails().getAccountNumber(), accountNumber));
    }

}
