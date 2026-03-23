package com.bank.design.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @JsonProperty("accountName")
    private String accountName;

    @JsonProperty("accountDetails")
    private AccountDetails accountDetails;

    @JsonProperty("accountPassword")
    private String accountPassword;

    public void addAccountDetails(AccountDetails accountDetails){
        this.accountDetails = accountDetails;
    }

    public User(String accountName, String accountPassword){
        this.accountName = accountName;
        this.accountPassword = accountPassword;
        this.accountDetails = new AccountDetails();
    }


}
