package com.basic.bank.controller;

import com.basic.bank.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/apply")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<String> applyForLoan(
            @RequestParam String accountNumber,
            @RequestParam BigDecimal amount,
            @RequestParam int months) {
        return ResponseEntity.ok(loanService.applyForLoan(accountNumber, amount,months));
    }

    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<?> getLoanStatus(@RequestParam String accountNumber) {
        return ResponseEntity.ok(loanService.getLoanStatus(accountNumber));
    }

    @PostMapping("/repay")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<String> repayLoan(
            @RequestParam String loanId,
            @RequestParam BigDecimal repaymentAmount) {
        return ResponseEntity.ok(loanService.repayLoan(loanId, repaymentAmount));
    }
    @PostMapping("/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveLoan(@RequestParam String loanRequestId) {
        return loanService.approveLoan(loanRequestId);
    }
}

