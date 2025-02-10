package com.basic.bank.service;

import com.basic.bank.entity.Account;
import com.basic.bank.entity.FixedDeposit;
import com.basic.bank.entity.Transaction;
import com.basic.bank.repository.AccountRepository;
import com.basic.bank.repository.FixedDepositRepository;
import com.basic.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional
    public FixedDeposit openFixedDeposit(String accountId, BigDecimal depositAmount, int months) {
        if (depositAmount.compareTo(BigDecimal.valueOf(1000)) < 0) {
            throw new IllegalArgumentException("Minimum deposit amount is 1000.");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));

        if (account.getBalance().compareTo(depositAmount) < 0) {
            throw new IllegalArgumentException("Not enough balance");
        }


        LocalDate startDate = LocalDate.now();
        LocalDate maturityDate = startDate.plusMonths(months);

        FixedDeposit fd = new FixedDeposit(accountId, depositAmount, startDate, maturityDate);

        account.setBalance(account.getBalance().subtract(depositAmount));
        fd = fixedDepositRepository.save(fd);
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountId);
        transaction.setType("FD_TRANSFER");
        transaction.setAmount(depositAmount);
        transactionRepository.save(transaction);
        account.getTransactions().add(transaction);
        account.getFixedDeposits().add(fd);
        accountRepository.save(account);
        return fd;

    }

    @Transactional
    public FixedDeposit closeFixedDeposit(String fdId) {
        FixedDeposit fd = fixedDepositRepository.findById(fdId)
                .orElseThrow(() -> new IllegalArgumentException("Fixed Deposit not found."));

        if ("CLOSED".equals(fd.getStatus())) {
            throw new IllegalStateException("FD already closed.");
        }

        LocalDate today = LocalDate.now();
        BigDecimal maturityAmount = fd.getDepositAmount();

        if (today.isBefore(fd.getMaturityDate())) {
            BigDecimal penalty = fd.getDepositAmount().multiply(BigDecimal.valueOf(0.01)); // 1% penalty
            maturityAmount = maturityAmount.subtract(penalty);
        }

        fd.setStatus("CLOSED");
        fd.setMaturityAmount(maturityAmount);


        Account account = accountRepository.findById(fd.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Associated account not found."));

        account.setBalance(account.getBalance().add(maturityAmount));



        Transaction transaction = new Transaction();
        transaction.setAccountNumber(fd.getAccountNumber());
        transaction.setType("FD_Credit");
        transaction.setAmount(maturityAmount);
        transactionRepository.save(transaction);
        account.getTransactions().add(transaction);
        accountRepository.save(account);

        return fixedDepositRepository.save(fd);
    }


    public FixedDeposit getFixedDepositById(String fdId) {
        return fixedDepositRepository.findById(fdId)
                .orElseThrow(() -> new IllegalArgumentException("Fixed Deposit not found."));
    }


    public List<FixedDeposit> getFixedDepositsByAccount(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));

        return account.getFixedDeposits();
    }

    public BigDecimal calculateMaturityAmount(String fdId) {
        FixedDeposit fd = getFixedDepositById(fdId);
        return fd.getMaturityAmount();
    }
}
