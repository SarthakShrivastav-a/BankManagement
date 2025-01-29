package com.basic.bank.controller;


import com.basic.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(transactionService.deposit(accountNumber, amount));
    }

    @PostMapping("/withdraw")
    public void withdraw(){}

    @PostMapping("/transfer")
    public void transfer(){}

    @GetMapping("/history")
    public void history(){}
}
