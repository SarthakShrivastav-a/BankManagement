package com.basic.bank.controller;


import com.basic.bank.service.AuthUserService;
import com.basic.bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private CustomerService customerService;


    @GetMapping("/history")
    public ResponseEntity<?> history(@RequestParam String account){
        return new ResponseEntity<>(customerService.getHistory(account), HttpStatus.ACCEPTED);
    }


}
