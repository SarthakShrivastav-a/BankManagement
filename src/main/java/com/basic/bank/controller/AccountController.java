package com.basic.bank.controller;


import com.basic.bank.entity.Customer;
import com.basic.bank.entity.Transaction;
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
import java.util.List;
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

    @GetMapping("/statements")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<?> getStatements(
            @RequestParam String account,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Transaction> transactions = customerService.getStatements(account, startDate, endDate);
        if (transactions.isEmpty()) {
            return new ResponseEntity<>("No transactions found within the given period.", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<BigDecimal> getBalance(@RequestParam String account) {
        BigDecimal balance = customerService.getBalance(account);
        if (balance != null) {
            return new ResponseEntity<>(balance, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
