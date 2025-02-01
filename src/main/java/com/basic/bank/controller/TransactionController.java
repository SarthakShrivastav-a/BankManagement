package com.basic.bank.controller;


import com.basic.bank.entity.Transaction;
import com.basic.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/deposit")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<String> deposit(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(transactionService.deposit(accountNumber, amount));
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<String> withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(transactionService.withdraw(accountNumber, amount));
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<String> transfer(
            @RequestParam String accountNumber,
            @RequestParam BigDecimal amount,
            @RequestParam String receiverNumber) {
        return ResponseEntity.ok(transactionService.transfer(accountNumber, amount, receiverNumber));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Transaction> getTransactionById(@RequestParam String id) {
        return new ResponseEntity<>(transactionService.getTransactionById(id), HttpStatus.ACCEPTED);
    }
}
