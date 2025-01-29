package com.basic.bank.service;

import com.basic.bank.entity.AuthUser;
import com.basic.bank.entity.Customer;
import com.basic.bank.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthUserService {

    @Autowired
    private AuthUserRepository authUserRepository;

    public void addCustomer(AuthUser authUser){
        authUserRepository.save(authUser);
    }

    public List<AuthUser> getall(){
        return authUserRepository.findAll();
    }
}
