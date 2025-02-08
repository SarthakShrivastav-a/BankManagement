package com.basic.bank.controller;


import com.basic.bank.entity.Customer;
import com.basic.bank.entity.Transaction;
import com.basic.bank.repository.CustomerRepository;
import com.basic.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerRepository customerRepository;

    private String getAccountNumberFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Customer> customerOpt = Optional.ofNullable(customerRepository.findByEmail(email));

        if (customerOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        return customerOpt.get().getAccountNumber();
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<String> deposit(@RequestParam BigDecimal amount) {
        String accountNumber = getAccountNumberFromToken();
        return ResponseEntity.ok(transactionService.deposit(accountNumber, amount));
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<String> withdraw(@RequestParam BigDecimal amount) {
        String accountNumber = getAccountNumberFromToken();
        return ResponseEntity.ok(transactionService.withdraw(accountNumber, amount));
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<String> transfer(@RequestParam BigDecimal amount, @RequestParam String receiverNumber) {
        String accountNumber = getAccountNumberFromToken();
        return ResponseEntity.ok(transactionService.transfer(accountNumber, amount, receiverNumber));
    }
}



//@RestController
//@RequestMapping("/api/transactions")
//public class TransactionController {
//
//    @Autowired
//    TransactionService transactionService;
//
//    @PostMapping("/deposit")
//    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
//    public ResponseEntity<String> deposit(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
//        return ResponseEntity.ok(transactionService.deposit(accountNumber, amount));
//    }
//
//    @PostMapping("/withdraw")
//    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
//    public ResponseEntity<String> withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
//        return ResponseEntity.ok(transactionService.withdraw(accountNumber, amount));
//    }
//
//    @PostMapping("/transfer")
//    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
//    public ResponseEntity<String> transfer(
//            @RequestParam String accountNumber,
//            @RequestParam BigDecimal amount,
//            @RequestParam String receiverNumber) {
//        return ResponseEntity.ok(transactionService.transfer(accountNumber, amount, receiverNumber));
//    }

//    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
//    public ResponseEntity<Transaction> getTransactionById(@RequestParam String id) {
//        return new ResponseEntity<>(transactionService.getTransactionById(id), HttpStatus.ACCEPTED);
//    }
//}
