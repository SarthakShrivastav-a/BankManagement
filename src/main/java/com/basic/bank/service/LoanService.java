package com.basic.bank.service;

import com.basic.bank.entity.Account;
import com.basic.bank.entity.Loan;
import com.basic.bank.repository.AccountRepository;
import com.basic.bank.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    public String applyForLoan(String accountNumber, BigDecimal amount) {

        Optional<Loan> existingLoan = loanRepository.findById(accountNumber);
        if (existingLoan.isPresent() && existingLoan.get().isActive()) {
            return "A loan already exists for this account.";
        }

        BigDecimal interestRate = new BigDecimal("0.05"); // Example 5% interest
        Loan loan = new Loan(accountNumber, amount, interestRate);
        loanRepository.save(loan);
        Account account = accountRepository.findById(accountNumber).get();
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        return "Loan successfully applied for account: " + accountNumber;
    }

    public String getLoanStatus(String accountNumber) {
        Optional<Loan> loan = loanRepository.findById(accountNumber);
        if (loan.isEmpty()) {
            return "No loan found for this account.";
        }
        Loan existingLoan = loan.get();
        return "Loan Status for account " + accountNumber + ": Remaining Balance: " +
                existingLoan.getRemainingBalance() + ", Due Date: " + existingLoan.getDueDate();
    }

    public String repayLoan(String accountNumber, BigDecimal repaymentAmount) {
        Optional<Loan> loanOpt = loanRepository.findById(accountNumber);
        if (loanOpt.isEmpty()) {
            return "No active loan found for this account.";
        }

        Loan loan = loanOpt.get();
        if (!loan.isActive()) {
            return "Loan for this account is already settled.";
        }

        Optional<Account> accountOpt = Optional.ofNullable(accountRepository.findByaccountNumber(accountNumber));
        if (accountOpt.isEmpty()) {
            return "Account not found.";
        }

        Account account = accountOpt.get();
        if (account.getBalance().compareTo(repaymentAmount) < 0) {
            return "Insufficient balance for loan repayment.";
        }

        BigDecimal remainingBalance = loan.getRemainingBalance();
        BigDecimal actualRepaymentAmount = repaymentAmount.compareTo(remainingBalance) > 0 ? remainingBalance : repaymentAmount;

        account.setBalance(account.getBalance().subtract(actualRepaymentAmount));
        accountRepository.save(account);

        remainingBalance = remainingBalance.subtract(actualRepaymentAmount);

        if (remainingBalance.compareTo(BigDecimal.ZERO) <= 0) {
            loan.setRemainingBalance(BigDecimal.ZERO);
            loan.setActive(false);
            loanRepository.save(loan);
            return "Loan fully repaid. No remaining balance.";
        }

        loan.setRemainingBalance(remainingBalance);
        loanRepository.save(loan);

        return "Repayment successful. Remaining Balance: " + remainingBalance;
    }

}
