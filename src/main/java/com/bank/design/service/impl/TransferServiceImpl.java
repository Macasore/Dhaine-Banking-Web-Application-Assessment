package com.bank.design.service.impl;

import com.bank.design.errorhandling.exceptions.GenericErrorCodeException;
import com.bank.design.model.dto.request.DepositRequest;
import com.bank.design.model.dto.request.WithdrawalRequest;
import com.bank.design.model.dto.response.BaseResponse;
import com.bank.design.model.entity.Transaction;
import com.bank.design.model.entity.User;
import com.bank.design.model.enums.ErrorCode;
import com.bank.design.model.enums.TransactionType;
import com.bank.design.service.TransferService;
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
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private static final BigDecimal MINIMUM_BALANCE = new BigDecimal("500.00");

    private final JsonUtil jsonUtil;

    @Override
    public BaseResponse<String> deposit(DepositRequest request) {
        try {
            Transaction latestTransaction = getLatestTransaction(request.getAccountNumber())
                    .orElse(new Transaction());

            Transaction newTransaction = Transaction.builder()
                    .accountNumber(request.getAccountNumber())
                    .amount(request.getAmount())
                    .newBalance(latestTransaction.getAmount() != null
                            ? latestTransaction.getNewBalance().add(request.getAmount())
                            : request.getAmount())
                    .previousBalance(latestTransaction.getNewBalance() != null
                            ? latestTransaction.getNewBalance()
                            : BigDecimal.ZERO)
                    .transactionType(TransactionType.CREDIT)
                    .createdAt(DateUtil.formatToString(LocalDateTime.now()))
                    .build();

            jsonUtil.addTransaction(newTransaction);

            return BaseResponse.success();
        } catch (GenericErrorCodeException e) {
            throw e;
        } catch (Exception e) {
            throw new GenericErrorCodeException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public BaseResponse<String> withdraw(WithdrawalRequest request) {
        try {
            String username = getAuthenticatedUsername();
            User user = jsonUtil.getUser(request.getAccountNumber());

            validateAccountIsForUser(username, user.getAccountName());

            if (!request.getPassword().equals(user.getAccountPassword())) {
                throw new GenericErrorCodeException(
                        "Invalid password provided",
                        ErrorCode.UNAUTHORIZED,
                        HttpStatus.UNAUTHORIZED
                );
            }

            Transaction latestTransaction = getLatestTransaction(request.getAccountNumber())
                    .orElse(new Transaction());

            BigDecimal currentBalance = latestTransaction.getNewBalance() != null
                    ? latestTransaction.getNewBalance()
                    : BigDecimal.ZERO;

            BigDecimal balanceAfterWithdrawal = currentBalance.subtract(request.getAmount());

            if (balanceAfterWithdrawal.compareTo(MINIMUM_BALANCE) < 0) {
                throw new GenericErrorCodeException(
                        "Insufficient funds. Account must have ₦500.00 after withdrawal.",
                        ErrorCode.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST
                );
            }

            Transaction newTransaction = Transaction.builder()
                    .accountNumber(request.getAccountNumber())
                    .amount(request.getAmount())
                    .newBalance(balanceAfterWithdrawal)
                    .previousBalance(currentBalance)
                    .transactionType(TransactionType.DEBIT)
                    .createdAt(DateUtil.formatToString(LocalDateTime.now()))
                    .build();

            jsonUtil.addTransaction(newTransaction);

            return BaseResponse.success();
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

    private void validateAccountIsForUser(String authenticatedUsername, String accountName) {
        if (!authenticatedUsername.equals(accountName)) {
            throw new GenericErrorCodeException(
                    "Account number passed doesn't match authenticated user",
                    ErrorCode.BAD_REQUEST,
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
