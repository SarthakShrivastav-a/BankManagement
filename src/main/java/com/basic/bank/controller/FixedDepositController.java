package com.basic.bank.controller;


import com.basic.bank.entity.FixedDeposit;
import com.basic.bank.service.FixedDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/fd")
public class FixedDepositController {

    @Autowired
    private FixedDepositService fixedDepositService;


    @PostMapping("/create")
    public FixedDeposit openFixedDeposit(@RequestParam String accountId,
                                         @RequestParam BigDecimal depositAmount,
                                         @RequestParam int months) {
        return fixedDepositService.openFixedDeposit(accountId, depositAmount, months);
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
    public List<FixedDeposit> getFixedDepositsByAccount(@RequestParam String accountId) {
        return fixedDepositService.getFixedDepositsByAccount(accountId);
    }

    @GetMapping("/maturity/{fdId}")
    public BigDecimal calculateMaturityAmount(@PathVariable String fdId) {
        return fixedDepositService.calculateMaturityAmount(fdId);
    }
}
