//package com.basic.bank.service;
//
//import com.basic.bank.entity.*;
//import com.basic.bank.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//@Service
//public class LoanService {
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private LoanRepository loanRepository;
//
//    @Autowired
//    private LoanRequestRepository loanRequestRepository;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private EmailService emailService;
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Transactional
//    public String applyForLoan(String accountNumber, BigDecimal amount) {
//        Optional<Account> accountOpt = accountRepository.findById(accountNumber);
//        Optional<Customer> customer = customerRepository.findById(accountNumber);
//        if (accountOpt.isEmpty()) {
//            return "Account not found.";
//        }
//
//        Optional<Loan> existingLoan = loanRepository.findById(accountNumber);
//        if (existingLoan.isPresent() && existingLoan.get().isActive()) {
//            return "A loan already exists for this account.";
//        }
//
//        LoanRequest loanRequest = new LoanRequest(accountNumber, amount, "PENDING");
//        loanRequestRepository.save(loanRequest);
//
//        String message = "Loan application submitted for account number " + accountNumber +
//                ". Loan request ID is " + loanRequest.getId() + ".";
//        emailService.sendEmail(customer.get().getEmail(), message, "Loan Application Submitted");
//
//        return "Loan request successfully submitted for account: " + accountNumber + ". Pending approval.";
//    }
//
//    public String approveLoan(String loanRequestId) {
//        Optional<LoanRequest> loanRequestOpt = loanRequestRepository.findById(loanRequestId);
//        if (loanRequestOpt.isEmpty()) {
//            return "Loan request not found.";
//        }
//
//        LoanRequest loanRequest = loanRequestOpt.get();
//        if (!"PENDING".equals(loanRequest.getStatus())) {
//            return "Loan request is already processed.";
//        }
//
//        Optional<Account> accountOpt = accountRepository.findById(loanRequest.getAccountNumber());
//        if (accountOpt.isEmpty()) {
//            return "Account not found for loan request.";
//        }
//
//        BigDecimal interestRate = new BigDecimal("0.05"); // Example 5% interest
//        Loan loan = new Loan(loanRequest.getAccountNumber(), loanRequest.getAmount(), interestRate);
//        loanRepository.save(loan);
//
//        Account account = accountOpt.get();
//
//        Transaction transaction = new Transaction();
//        transaction.setAccountNumber(loanRequest.getAccountNumber());
//        transaction.setType("Loan_Credit");
//        transaction.setAmount(loanRequest.getAmount());
//        transactionRepository.save(transaction);
//
//        account.getLoans().add(loan);
//        account.getTransactions().add(transaction);
//        account.setBalance(account.getBalance().add(loanRequest.getAmount()));
//        accountRepository.save(account);
//
//        loanRequest.setStatus("APPROVED");
//        loanRequestRepository.save(loanRequest);
//
//        Optional<Customer> customer = customerRepository.findById(loanRequest.getAccountNumber());
//
//        String message = "Your loan request " + loanRequestId + " has been approved.";
//        emailService.sendEmail(customer.get().getEmail(), message, "Loan Approval");
//
//        return "Loan successfully approved and credited for account: " + loanRequest.getAccountNumber();
//    }
//
//    public String repayLoan(String accountNumber, BigDecimal repaymentAmount) {
//        Optional<Loan> loanOpt = loanRepository.findById(accountNumber);
//
//        if (loanOpt.isEmpty()) {
//            return "No active loan found for this account.";
//        }
//
//        Loan loan = loanOpt.get();
//        if (!loan.isActive()) {
//            return "Loan for this account is already settled.";
//        }
//
//        Optional<Account> accountOpt = accountRepository.findById(accountNumber);
//        if (accountOpt.isEmpty()) {
//            return "Account not found.";
//        }
//
//        Account account = accountOpt.get();
//        if (account.getBalance().compareTo(repaymentAmount) < 0) {
//            return "Insufficient balance for loan repayment.";
//        }
//
//        BigDecimal remainingBalance = loan.getRemainingBalance();
//        BigDecimal actualRepaymentAmount = repaymentAmount.compareTo(remainingBalance) > 0 ? remainingBalance : repaymentAmount;
//
//        Transaction transaction = new Transaction();
//        transaction.setAccountNumber(accountNumber);
//        transaction.setType("Loan_Debit");
//        transaction.setAmount(actualRepaymentAmount);
//        transactionRepository.save(transaction);
//
//        account.setBalance(account.getBalance().subtract(actualRepaymentAmount));
//        account.getTransactions().add(transaction);
//        accountRepository.save(account);
//
//        remainingBalance = remainingBalance.subtract(actualRepaymentAmount);
//
//        Optional<Customer> customer = customerRepository.findById(accountNumber);
//
//        String repaymentMessage = "Loan repayment of INR " + actualRepaymentAmount + " completed.";
//        emailService.sendEmail(customer.get().getEmail(), repaymentMessage, "Loan Repayment");
//
//        if (remainingBalance.compareTo(BigDecimal.ZERO) <= 0) {
//            loan.setRemainingBalance(BigDecimal.ZERO);
//            loan.setActive(false);
//            loanRepository.save(loan);
//
//            String completionMessage = "Loan fully repaid. No remaining balance.";
//            emailService.sendEmail(customer.get().getEmail(), completionMessage, "Loan Repayment Completion");
//            return completionMessage;
//        }
//
//        loan.setRemainingBalance(remainingBalance);
//        loanRepository.save(loan);
//
//        return repaymentMessage + " Remaining Balance: " + remainingBalance;
//    }
//    public String getLoanStatus(String accountNumber) {
//        Optional<Loan> loan = loanRepository.findById(accountNumber);
//        if (loan.isEmpty()) {
//            return "No loan found for this account.";
//        }
//        Loan existingLoan = loan.get();
//        return "Loan Status for account " + accountNumber + ": Remaining Balance: " +
//                existingLoan.getRemainingBalance() + ", Due Date: " + existingLoan.getDueDate();
//    }
//}
package com.basic.bank.service;

import com.basic.bank.entity.*;
import com.basic.bank.repository.*;
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

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PdfService pdfService;

        @Transactional
    public String applyForLoan(String accountNumber, BigDecimal amount) {
        Optional<Account> accountOpt = accountRepository.findById(accountNumber);
        Optional<Customer> customer = customerRepository.findById(accountNumber);
        if (accountOpt.isEmpty()) {
            return "Account not found.";
        }

        Optional<Loan> existingLoan = loanRepository.findById(accountNumber);
        if (existingLoan.isPresent() && existingLoan.get().isActive()) {
            return "A loan already exists for this account.";
        }

        LoanRequest loanRequest = new LoanRequest(accountNumber, amount, "PENDING");
        loanRequestRepository.save(loanRequest);

        String message = "Loan application submitted for account number " + accountNumber +
                ". Loan request ID is " + loanRequest.getId() + ".";
        emailService.sendEmail(customer.get().getEmail(), message, "Loan Application Submitted");

        return "Loan request successfully submitted for account: " + accountNumber + ". Pending approval.";
    }

    @Transactional
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

        BigDecimal interestRate = new BigDecimal("0.05");
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

        Optional<Customer> customer = customerRepository.findById(loanRequest.getAccountNumber());
//        String receiptPath = pdfService.generateLoanTransactionReceipt(account.getAccountNumber(),loanRequestId,);
        emailService.sendEmail(customer.get().getEmail(), ("Your loan Request with ID " +loanRequestId +" has been approved. Amount has been credited to your account."), "Loan Approval");

        return "Loan successfully approved and credited for account: " + loanRequest.getAccountNumber();
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

        Optional<Customer> customer = customerRepository.findById(accountNumber);
        byte [] pdf = pdfService.generateLoanRepaymentReceipt(accountNumber,loanOpt.get().getAccountNumber(),repaymentAmount,remainingBalance);
        emailService.sendEmailWithAttachment(customer.get().getEmail(), "Loan repayment receipt", "Loan Repayment", pdf,"Loan Receipt");

        if (remainingBalance.compareTo(BigDecimal.ZERO) <= 0) {
            loan.setRemainingBalance(BigDecimal.ZERO);
            loan.setActive(false);
            loanRepository.save(loan);
            pdfService.generateLoanCompletionReceipt(accountNumber,loanOpt.get().getAccountNumber());
            return "Loan fully repaid. No remaining balance.";
        }

        loan.setRemainingBalance(remainingBalance);
        loanRepository.save(loan);
        return "Loan repayment successful. Remaining Balance: " + remainingBalance;
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
}
