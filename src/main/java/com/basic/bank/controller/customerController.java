package com.basic.bank.controller;


import com.basic.bank.entity.AuthUser;
import com.basic.bank.entity.Customer;
import com.basic.bank.entity.SignUp;
import com.basic.bank.service.AuthUserService;
import com.basic.bank.service.CustomerService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
public class customerController {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private CustomerService customerService;


    @PostMapping("/create")
    public ResponseEntity<SignUp> createCustomer(@Valid @RequestBody SignUp signUp){
        customerService.addCustomer(signUp);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
