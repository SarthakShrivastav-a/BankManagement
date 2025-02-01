package com.basic.bank.controller;


import com.basic.bank.entity.Customer;
import com.basic.bank.service.AuthService;
import com.basic.bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AuthService authUserService;

    @Autowired
    private CustomerService customerService;


    @GetMapping("/history")
    public ResponseEntity<?> history(@RequestParam String account){
        return new ResponseEntity<>(customerService.getHistory(account), HttpStatus.ACCEPTED);
    }

    @GetMapping("/info")
    public ResponseEntity<Optional<Customer>> getDetails(@RequestParam String account){
        return new ResponseEntity<>(customerService.getCustomer(account),HttpStatus.OK);
    }

}
