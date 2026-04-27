package com.bank.design.service.impl;

import com.bank.design.errorhandling.exceptions.GenericErrorCodeException;
import com.bank.design.model.dto.response.AccountInfoResponse;
import com.bank.design.model.dto.response.AccountStatementResponse;
import com.bank.design.model.dto.response.BaseResponse;
import com.bank.design.model.entity.Transaction;
import com.bank.design.model.entity.User;
import com.bank.design.model.enums.ErrorCode;
import com.bank.design.service.AccountService;
import com.bank.design.utils.DateUtil;
import com.bank.design.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final JsonUtil jsonUtil;

    @Override
    public BaseResponse<AccountInfoResponse> getAccountInfo(String accountNumber) {
        try {
            String username = getAuthenticatedUsername();
            User user = jsonUtil.getUser(accountNumber);

            validateAccountOwnership(username, user.getAccountName());

            BigDecimal currentBalance = getLatestTransaction(accountNumber)
                    .map(Transaction::getNewBalance)
                    .orElse(BigDecimal.ZERO);

            AccountInfoResponse response = AccountInfoResponse.builder()
                    .accountName(user.getAccountName())
                    .accountNumber(user.getAccountDetails().getAccountNumber())
                    .accountBalance(currentBalance)
                    .build();

            return BaseResponse.success(response);
        } catch (GenericErrorCodeException e) {
            throw e;
        } catch (Exception e) {
            throw new GenericErrorCodeException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public BaseResponse<List<AccountStatementResponse>> getAccountStatement(String accountNumber) {
        try {
            String username = getAuthenticatedUsername();
            User user = jsonUtil.getUser(accountNumber);

            validateAccountOwnership(username, user.getAccountName());

            List<AccountStatementResponse> statement = jsonUtil.getTransactionsForUser(accountNumber).stream()
                    .sorted(Comparator.comparing((Transaction t) -> DateUtil.formatToLocalDateTime(t.getCreatedAt())).reversed())
                    .map(t -> AccountStatementResponse.builder()
                            .transactionDate(t.getCreatedAt())
                            .transactionType(t.getTransactionType())
                            .narration(t.getNarration())
                            .amount(t.getAmount())
                            .accountBalance(t.getNewBalance())
                            .build())
                    .toList();

            return BaseResponse.success(statement);
        } catch (GenericErrorCodeException e) {
            throw e;
        } catch (Exception e) {
            throw new GenericErrorCodeException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Optional<Transaction> getLatestTransaction(String accountNumber) {
        try {
            return jsonUtil.getTransactionsForUser(accountNumber).stream()
                    .sorted(Comparator.comparing((Transaction t) -> DateUtil.formatToLocalDateTime(t.getCreatedAt())).reversed())
                    .findFirst();
        } catch (GenericErrorCodeException e) {
            throw e;
        } catch (Exception e) {
            throw new GenericErrorCodeException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        assert userDetails != null;
        return userDetails.getUsername();
    }

    private void validateAccountOwnership(String authenticatedUsername, String accountName) {
        if (!authenticatedUsername.equals(accountName)) {
            throw new GenericErrorCodeException(
                    "Account number passed doesn't match authenticated user",
                    ErrorCode.BAD_REQUEST,
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
