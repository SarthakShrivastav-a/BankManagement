package com.basic.bank.controller;
import com.basic.bank.entity.Account;
import com.basic.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/blockAccount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> blockAccount(@RequestParam String accountNumber) {
        Optional<Account> accountOpt = accountRepository.findById(accountNumber);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setBlocked(true);
            accountRepository.save(account);
            return ResponseEntity.ok("Account " + accountNumber + " has been blocked successfully.");
        }
        return ResponseEntity.badRequest().body("Account not found.");
    }

    @PostMapping("/unblockAccount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> unblockAccount(@RequestParam String accountNumber) {
        Optional<Account> accountOpt = accountRepository.findById(accountNumber);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setBlocked(false);
            accountRepository.save(account);
            return ResponseEntity.ok("Account " + accountNumber + " has been unblocked successfully.");
        }
        return ResponseEntity.badRequest().body("Account not found.");
    }
}

