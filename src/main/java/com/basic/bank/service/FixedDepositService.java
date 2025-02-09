package com.basic.bank.service;

import com.basic.bank.entity.Account;
import com.basic.bank.entity.FixedDeposit;
import com.basic.bank.repository.AccountRepository;
import com.basic.bank.repository.FixedDepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FixedDepositService {

    @Autowired
    private FixedDepositRepository fixedDepositRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Open a new Fixed Deposit (FD) for an account.
     */
    public FixedDeposit openFixedDeposit(String accountId, BigDecimal depositAmount, int months) {
        if (depositAmount.compareTo(BigDecimal.valueOf(1000)) < 0) {
            throw new IllegalArgumentException("Minimum deposit amount is 1000.");
        }

        LocalDate startDate = LocalDate.now();
        LocalDate maturityDate = startDate.plusMonths(months);

        // Create and save FD
        FixedDeposit fd = new FixedDeposit(accountId, depositAmount, startDate, maturityDate);
        FixedDeposit savedFD = fixedDepositRepository.save(fd);

        // Link FD to account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));

        account.getFixedDeposits().add(savedFD);
        accountRepository.save(account);

        return savedFD;
    }

    /**
     * Close an existing FD before maturity, with penalty on interest.
     */
    public FixedDeposit closeFixedDeposit(String fdId) {
        FixedDeposit fd = fixedDepositRepository.findById(fdId)
                .orElseThrow(() -> new IllegalArgumentException("Fixed Deposit not found."));

        if ("CLOSED".equals(fd.getStatus())) {
            throw new IllegalStateException("FD already closed.");
        }

        fd.setStatus("CLOSED");

        // Apply penalty for early closure if not matured
        LocalDate today = LocalDate.now();
        if (today.isBefore(fd.getMaturityDate())) {
            BigDecimal penalty = fd.getDepositAmount().multiply(BigDecimal.valueOf(0.01)); // 1% penalty
            fd.setMaturityAmount(fd.getDepositAmount().subtract(penalty));
        }

        return fixedDepositRepository.save(fd);
    }

    /**
     * Get the details of an FD by FD ID.
     */
    public FixedDeposit getFixedDepositById(String fdId) {
        return fixedDepositRepository.findById(fdId)
                .orElseThrow(() -> new IllegalArgumentException("Fixed Deposit not found."));
    }

    /**
     * Get all active FDs for a specific account.
     */
    public List<FixedDeposit> getFixedDepositsByAccount(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));

        return account.getFixedDeposits();
    }

    /**
     * Calculate and return the maturity amount of a given FD.
     */
    public BigDecimal calculateMaturityAmount(String fdId) {
        FixedDeposit fd = getFixedDepositById(fdId);
        return fd.getMaturityAmount();
    }
}
