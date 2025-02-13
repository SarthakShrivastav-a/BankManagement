package com.basic.bank.controller;


import com.basic.bank.entity.Customer;
import com.basic.bank.entity.ProfileResponse;
import com.basic.bank.entity.Transaction;
import com.basic.bank.repository.CustomerRepository;
import com.basic.bank.service.AuthService;
import com.basic.bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<?> history() {
        String account = getAccountNumberFromToken();
        return new ResponseEntity<>(customerService.getHistory(account), HttpStatus.ACCEPTED);
    }

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Optional<Customer>> getDetails() {
        String account = getAccountNumberFromToken();
        return new ResponseEntity<>(customerService.getCustomer(account), HttpStatus.OK);
    }

    @GetMapping("/statements")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<?> getStatements(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        String account = getAccountNumberFromToken();
        List<Transaction> transactions = customerService.getStatements(account, startDate, endDate);
        if (transactions.isEmpty()) {
            return new ResponseEntity<>("No transactions found within the given period.", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<BigDecimal> getBalance() {
        String account = getAccountNumberFromToken();
        BigDecimal balance = customerService.getBalance(account);
        if (balance != null) {
            return new ResponseEntity<>(balance, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
        private String getAccountNumberFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Customer> customerOpt = Optional.ofNullable(customerRepository.findByEmail(email));

        if (customerOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        return customerOpt.get().getAccountNumber();
    }


    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<ProfileResponse> getProfile() {
        String account = getAccountNumberFromToken();
        Optional<Customer> customerOpt = customerService.getCustomer(account);
        List<Transaction> history = customerService.getHistory(account);
        BigDecimal balance = customerService.getBalance(account);
//        List<Transaction> statements = customerService.getStatements(account, startDate, endDate);

        if (customerOpt.isPresent()) {
            ProfileResponse profileResponse = new ProfileResponse(
                    customerOpt.get(),
                    history,
                    balance

            );
            return new ResponseEntity<>(profileResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
