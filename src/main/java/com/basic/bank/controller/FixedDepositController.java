package com.basic.bank.controller;


import com.basic.bank.entity.Customer;
import com.basic.bank.entity.FixedDeposit;
import com.basic.bank.repository.CustomerRepository;
import com.basic.bank.service.FixedDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fd")
public class FixedDepositController {

    @Autowired
    private FixedDepositService fixedDepositService;

    @Autowired
    private CustomerRepository customerRepository;


    @PostMapping("/create")
    public FixedDeposit openFixedDeposit(
                                         @RequestParam BigDecimal amount,
                                         @RequestParam int months) {
        String accountNumber = getAccountNumberFromToken();
        return fixedDepositService.openFixedDeposit(accountNumber, amount, months);
    }

    @PostMapping("/close/{fdId}")
    public FixedDeposit closeFixedDeposit(@PathVariable String fdId) {
        return fixedDepositService.closeFixedDeposit(fdId);
    }

    @GetMapping("/{fdId}")
    public FixedDeposit getFixedDepositById(@PathVariable String fdId) {
        return fixedDepositService.getFixedDepositById(fdId);
    }

    @GetMapping("/all")
    public List<FixedDeposit> getFixedDepositsByAccount() {
        String accountNumber = getAccountNumberFromToken();
        return fixedDepositService.getFixedDepositsByAccount(accountNumber);
    }

    @GetMapping("/maturity/{fdId}")
    public BigDecimal calculateMaturityAmount(@PathVariable String fdId) {
        return fixedDepositService.calculateMaturityAmount(fdId);
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
}
