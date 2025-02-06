package com.basic.bank.service;

import com.basic.bank.entity.Account;
import com.basic.bank.entity.Loan;
import com.basic.bank.entity.LoanRequest;
import com.basic.bank.entity.Transaction;
import com.basic.bank.repository.AccountRepository;
import com.basic.bank.repository.LoanRepository;
import com.basic.bank.repository.LoanRequestRepository;
import com.basic.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public String applyForLoan(String accountNumber, BigDecimal amount) {
        Optional<Account> accountOpt = accountRepository.findById(accountNumber);
        if (accountOpt.isEmpty()) {
            return "Account not found.";
        }

        Optional<Loan> existingLoan = loanRepository.findById(accountNumber);
        if (existingLoan.isPresent() && existingLoan.get().isActive()) {
            return "A loan already exists for this account.";
        }

        LoanRequest loanRequest = new LoanRequest(accountNumber, amount, "PENDING");
        loanRequestRepository.save(loanRequest);

        return "Loan request successfully submitted for account: " + accountNumber + ". Pending approval.";
    }

    public String approveLoan(String loanRequestId) {
        Optional<LoanRequest> loanRequestOpt = loanRequestRepository.findById(loanRequestId);
        if (loanRequestOpt.isEmpty()) {
            return "Loan request not found.";
        }

        LoanRequest loanRequest = loanRequestOpt.get();
        if (!"PENDING".equals(loanRequest.getStatus())) {
            return "Loan request is already processed.";
        }

        Optional<Account> accountOpt = accountRepository.findById(loanRequest.getAccountNumber());
        if (accountOpt.isEmpty()) {
            return "Account not found for loan request.";
        }

        BigDecimal interestRate = new BigDecimal("0.05"); // Example 5% interest
        Loan loan = new Loan(loanRequest.getAccountNumber(), loanRequest.getAmount(), interestRate);
        loanRepository.save(loan);

        Account account = accountOpt.get();

        Transaction transaction = new Transaction();
        transaction.setAccountNumber(loanRequest.getAccountNumber());
        transaction.setType("Loan_Credit");
        transaction.setAmount(loanRequest.getAmount());
        transactionRepository.save(transaction);

        account.getLoans().add(loan);
        account.getTransactions().add(transaction);
        account.setBalance(account.getBalance().add(loanRequest.getAmount()));
        accountRepository.save(account);

        loanRequest.setStatus("APPROVED");
        loanRequestRepository.save(loanRequest);

        return "Loan successfully approved and credited for account: " + loanRequest.getAccountNumber();
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

        Optional<Account> accountOpt = accountRepository.findById(accountNumber);
        if (accountOpt.isEmpty()) {
            return "Account not found.";
        }

        Account account = accountOpt.get();
        if (account.getBalance().compareTo(repaymentAmount) < 0) {
            return "Insufficient balance for loan repayment.";
        }

        BigDecimal remainingBalance = loan.getRemainingBalance();
        BigDecimal actualRepaymentAmount = repaymentAmount.compareTo(remainingBalance) > 0 ? remainingBalance : repaymentAmount;

        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setType("Loan_Debit");
        transaction.setAmount(actualRepaymentAmount);
        transactionRepository.save(transaction);

        account.setBalance(account.getBalance().subtract(actualRepaymentAmount));
        account.getTransactions().add(transaction);
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
