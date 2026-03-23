package com.bank.design;

import com.bank.design.model.dto.request.CreateAccountRequest;
import com.bank.design.model.entity.AccountDetails;
import com.bank.design.model.entity.User;
import com.bank.design.utils.JsonUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DesignApplication {

	public static void main(String[] args){SpringApplication.run(DesignApplication.class, args);}

}
