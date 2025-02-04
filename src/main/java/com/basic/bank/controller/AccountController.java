package com.basic.bank.controller;


import com.basic.bank.entity.Customer;
import com.basic.bank.service.AuthService;
import com.basic.bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AuthService authUserService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<?> history(@RequestParam String account) {
        return new ResponseEntity<>(customerService.getHistory(account), HttpStatus.ACCEPTED);
    }

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Optional<Customer>> getDetails(@RequestParam String account) {
        return new ResponseEntity<>(customerService.getCustomer(account), HttpStatus.OK);
    }

//    @GetMapping("/balance")
//    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER') and @customerAuthorizationService.isAccountOwner(#account)")
//    public ResponseEntity<BigDecimal> getBalance(@RequestParam String account) {
//        return new ResponseEntity<>(customerService.getBalance(account), HttpStatus.OK);
//    }
//
//    @GetMapping("/statements")
//    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER') and @customerAuthorizationService.isAccountOwner(#account)")
//    public ResponseEntity<?> getStatements(
//            @RequestParam String account,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
//        return new ResponseEntity<>(customerService.getStatements(account, startDate, endDate), HttpStatus.OK);
//    }
}
