package com.bank.design.service;

import com.bank.design.model.entity.User;
import com.bank.design.service.impl.UserAuthServiceImpl;
import com.bank.design.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserDetails implements UserDetailsService {

    private final JsonUtil jsonUtil;
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        try {
            User user = jsonUtil.getUser(username);
            List<String> roles = new ArrayList<>();
            roles.add("USER");

            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getAccountName())
                    .password(user.getAccountPassword())
                    .roles(roles.toArray(new String[0]))
                    .build();

            return userDetails;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
