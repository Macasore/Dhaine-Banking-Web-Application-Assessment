package com.bank.design.utils;

import com.bank.design.errorhandling.exceptions.GenericErrorCodeException;
import com.bank.design.model.entity.Transaction;
import com.bank.design.model.entity.User;
import com.bank.design.model.enums.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonUtil {
    private static final String USER_DATA_JSON = "./user_data.json";
    private static final String TRANSACTION_DATA_JSON = "./transaction_data.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createUserObject(User request) throws IOException {
        File file = new File(USER_DATA_JSON);

        List<User> userList = new ArrayList<>();

        if (file.exists() && file.length() > 0){
            userList = objectMapper.readValue(file, new TypeReference<List<User>>() {});
        }
        userList.add(request);

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, userList);
        log.info("User saved successfully. Total users: {}", userList.size());

    }

    public List<User> returnUsers() throws IOException {
        File file = new File(USER_DATA_JSON);
        if (!file.exists() || file.length() ==0){
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, new TypeReference<List<User>>() {});
    }

    public User getUser(String accountNumber) throws IOException {
        User user = returnUsers().stream()
                .filter(u -> Objects.equals(u.getAccountDetails().getAccountNumber(), accountNumber))
                .findFirst()
                .orElseThrow(() -> new GenericErrorCodeException("Invalid account number or password provided", ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST));
        return user;
    }


    public void addTransaction(Transaction newTransaction) throws IOException {
        File file = new File(TRANSACTION_DATA_JSON);

        List<Transaction> transactionList = new ArrayList<>();

        if (file.exists() && file.length() > 0){
            transactionList = objectMapper.readValue(file, new TypeReference<List<Transaction>>() {});
        }
        transactionList.add(newTransaction);

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, transactionList);
        log.info("Transaction saved successfully. Total transactions: {}", transactionList.size());
    }

    public List<Transaction> returnTransactions() throws IOException {
        File file = new File(TRANSACTION_DATA_JSON);
        if (!file.exists() || file.length() ==0){
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, new TypeReference<List<Transaction>>() {});
    }

    public List<Transaction> getTransactionsForUser(String accountNumber) throws IOException {
        return returnTransactions().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .toList();

    }
}
