//package com.basic.bank.service;
//
//import com.basic.bank.entity.*;
//import com.basic.bank.repository.*;
//import com.basic.bank.service.CustomerService;
//import com.basic.bank.service.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
////package com.basic.bank.service;
////
////import com.basic.bank.entity.*;
////import com.basic.bank.repository.*;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////
////import java.math.BigDecimal;
////import java.util.Optional;
////
////@Service
////public class LoanService {
////
////    @Autowired
////    private TransactionRepository transactionRepository;
////
////    @Autowired
////    private LoanRepository loanRepository;
////
////    @Autowired
////    private LoanRequestRepository loanRequestRepository;
////
////    @Autowired
////    private AccountRepository accountRepository;
////
////    @Autowired
////    private EmailService emailService;
////
////    @Autowired
////    private CustomerRepository customerRepository;
////
////    @Transactional
////    public String applyForLoan(String accountNumber, BigDecimal amount) {
////        Optional<Account> accountOpt = accountRepository.findById(accountNumber);
////        Optional<Customer> customer = customerRepository.findById(accountNumber);
////        if (accountOpt.isEmpty()) {
////            return "Account not found.";
////        }
////
////        Optional<Loan> existingLoan = loanRepository.findById(accountNumber);
////        if (existingLoan.isPresent() && existingLoan.get().isActive()) {
////            return "A loan already exists for this account.";
////        }
////
////        LoanRequest loanRequest = new LoanRequest(accountNumber, amount, "PENDING");
////        loanRequestRepository.save(loanRequest);
////
////        String message = "Loan application submitted for account number " + accountNumber +
////                ". Loan request ID is " + loanRequest.getId() + ".";
////        emailService.sendEmail(customer.get().getEmail(), message, "Loan Application Submitted");
////
////        return "Loan request successfully submitted for account: " + accountNumber + ". Pending approval.";
////    }
////
////    public String approveLoan(String loanRequestId) {
////        Optional<LoanRequest> loanRequestOpt = loanRequestRepository.findById(loanRequestId);
////        if (loanRequestOpt.isEmpty()) {
////            return "Loan request not found.";
////        }
////
////        LoanRequest loanRequest = loanRequestOpt.get();
////        if (!"PENDING".equals(loanRequest.getStatus())) {
////            return "Loan request is already processed.";
////        }
////
////        Optional<Account> accountOpt = accountRepository.findById(loanRequest.getAccountNumber());
////        if (accountOpt.isEmpty()) {
////            return "Account not found for loan request.";
////        }
////
////        BigDecimal interestRate = new BigDecimal("0.05"); // Example 5% interest
////        Loan loan = new Loan(loanRequest.getAccountNumber(), loanRequest.getAmount(), interestRate);
////        loanRepository.save(loan);
////
////        Account account = accountOpt.get();
////
////        Transaction transaction = new Transaction();
////        transaction.setAccountNumber(loanRequest.getAccountNumber());
////        transaction.setType("Loan_Credit");
////        transaction.setAmount(loanRequest.getAmount());
////        transactionRepository.save(transaction);
////
////        account.getLoans().add(loan);
////        account.getTransactions().add(transaction);
////        account.setBalance(account.getBalance().add(loanRequest.getAmount()));
////        accountRepository.save(account);
////
////        loanRequest.setStatus("APPROVED");
////        loanRequestRepository.save(loanRequest);
////
////        Optional<Customer> customer = customerRepository.findById(loanRequest.getAccountNumber());
////
////        String message = "Your loan request " + loanRequestId + " has been approved.";
////        emailService.sendEmail(customer.get().getEmail(), message, "Loan Approval");
////
////        return "Loan successfully approved and credited for account: " + loanRequest.getAccountNumber();
////    }
////
////    public String repayLoan(String accountNumber, BigDecimal repaymentAmount) {
////        Optional<Loan> loanOpt = loanRepository.findById(accountNumber);
////
////        if (loanOpt.isEmpty()) {
////            return "No active loan found for this account.";
////        }
////
////        Loan loan = loanOpt.get();
////        if (!loan.isActive()) {
////            return "Loan for this account is already settled.";
////        }
////
////        Optional<Account> accountOpt = accountRepository.findById(accountNumber);
////        if (accountOpt.isEmpty()) {
////            return "Account not found.";
////        }
////
////        Account account = accountOpt.get();
////        if (account.getBalance().compareTo(repaymentAmount) < 0) {
////            return "Insufficient balance for loan repayment.";
////        }
////
////        BigDecimal remainingBalance = loan.getRemainingBalance();
////        BigDecimal actualRepaymentAmount = repaymentAmount.compareTo(remainingBalance) > 0 ? remainingBalance : repaymentAmount;
////
////        Transaction transaction = new Transaction();
////        transaction.setAccountNumber(accountNumber);
////        transaction.setType("Loan_Debit");
////        transaction.setAmount(actualRepaymentAmount);
////        transactionRepository.save(transaction);
////
////        account.setBalance(account.getBalance().subtract(actualRepaymentAmount));
////        account.getTransactions().add(transaction);
////        accountRepository.save(account);
////
////        remainingBalance = remainingBalance.subtract(actualRepaymentAmount);
////
////        Optional<Customer> customer = customerRepository.findById(accountNumber);
////
////        String repaymentMessage = "Loan repayment of INR " + actualRepaymentAmount + " completed.";
////        emailService.sendEmail(customer.get().getEmail(), repaymentMessage, "Loan Repayment");
////
////        if (remainingBalance.compareTo(BigDecimal.ZERO) <= 0) {
////            loan.setRemainingBalance(BigDecimal.ZERO);
////            loan.setActive(false);
////            loanRepository.save(loan);
////
////            String completionMessage = "Loan fully repaid. No remaining balance.";
////            emailService.sendEmail(customer.get().getEmail(), completionMessage, "Loan Repayment Completion");
////            return completionMessage;
////        }
////
////        loan.setRemainingBalance(remainingBalance);
////        loanRepository.save(loan);
////
////        return repaymentMessage + " Remaining Balance: " + remainingBalance;
////    }
////    public String getLoanStatus(String accountNumber) {
////        Optional<Loan> loan = loanRepository.findById(accountNumber);
////        if (loan.isEmpty()) {
////            return "No loan found for this account.";
////        }
////        Loan existingLoan = loan.get();
////        return "Loan Status for account " + accountNumber + ": Remaining Balance: " +
////                existingLoan.getRemainingBalance() + ", Due Date: " + existingLoan.getDueDate();
////    }
////}
//
//
//
//@Service
//public class LoanService {
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
//    private TransactionRepository transactionRepository;
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
//        if (accountOpt.isEmpty()) {
//            return "Account not found.";
//        }
//
//        LoanRequest loanRequest = new LoanRequest(accountNumber, amount, "PENDING");
//        loanRequestRepository.save(loanRequest);
//
//        Optional<Customer>customerOpt = customerRepository.findById(accountNumber);
//
//        String message = "Loan application submitted for account number " + accountNumber +
//                ". Loan request ID is " + loanRequest.getId() + ".";
//        emailService.sendEmail(customerOpt.get().getEmail(), message, "Loan Application Submitted");
//
//        return "Loan request successfully submitted for account: " + accountNumber + ". Pending approval.";
//    }
//
//    @Transactional
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
//        BigDecimal interestRate = new BigDecimal("0.05");
//        Loan loan = new Loan(loanRequest.getAccountNumber(), loanRequest.getAmount(), interestRate);
//        loanRepository.save(loan);
//
//        Account account = accountOpt.get();
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
//        Optional<Customer>customerOpt = customerRepository.findById(loanRequest.getAccountNumber());
//
//        emailService.sendEmail(customerOpt.get().getEmail(), "Your loan request " + loanRequestId + " has been approved.", "Loan Approval");
//
//        return "Loan successfully approved and credited for account: " + loanRequest.getAccountNumber();
//    }
//
//    public String repayLoan(String loanId, BigDecimal repaymentAmount) {
//        Optional<Loan> loanOpt = loanRepository.findById(loanId);
//
//        if (loanOpt.isEmpty()) {
//            return "No active loan found with this ID.";
//        }
//
//        Loan loan = loanOpt.get();
//        if (!loan.isActive()) {
//            return "Loan is already settled.";
//        }
//
//        Optional<Account> accountOpt = accountRepository.findById(loan.getAccountNumber());
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
//        transaction.setAccountNumber(loan.getAccountNumber());
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
//        Optional<Customer>customerOpt = customerRepository.findById(loan.getAccountNumber());
//
//        emailService.sendEmail(customerOpt.get().getEmail(), "Loan repayment of INR " + actualRepaymentAmount + " completed.", "Loan Repayment");
//
//        if (remainingBalance.compareTo(BigDecimal.ZERO) <= 0) {
//            loan.setRemainingBalance(BigDecimal.ZERO);
//            loan.setActive(false);
//            loanRepository.save(loan);
//
//            emailService.sendEmail(customerOpt.get().getEmail(), "Loan fully repaid. No remaining balance.", "Loan Repayment Completion");
//            return "Loan fully repaid. No remaining balance.";
//        }
//
//        loan.setRemainingBalance(remainingBalance);
//        loanRepository.save(loan);
//
//        return "Loan repayment successful. Remaining Balance: " + remainingBalance;
//    }
//
//    public String getLoanStatus(String loanId) {
//        Optional<Loan> loanOpt = loanRepository.findById(loanId);
//        if (loanOpt.isEmpty()) {
//            return "No loan found with this ID.";
//        }
//        Loan loan = loanOpt.get();
//        return "Loan Status: Remaining Balance: " + loan.getRemainingBalance() + ", Due Date: " + loan.getDueDate();
//    }
//}
//
//
package com.basic.bank.service;

import com.basic.bank.entity.*;
import com.basic.bank.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerRepository customerRepository;

    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);

    @Transactional
    public String applyForLoan(String accountNumber, BigDecimal amount, int termMonths) {
        Optional<Account> accountOpt = accountRepository.findById(accountNumber);
        if (accountOpt.isEmpty()) {
            return "Account not found.";
        }

        LoanRequest loanRequest = new LoanRequest(accountNumber, amount, "PENDING");
        loanRequestRepository.save(loanRequest);

        Optional<Customer> customerOpt = customerRepository.findById(accountNumber);

        String message = "Loan application submitted for account number " + accountNumber +
                ". Loan request ID is " + loanRequest.getId() + ".";
        emailService.sendEmail(customerOpt.get().getEmail(), message, "Loan Application Submitted");

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

        int loanTermMonths = 12; // Default term (Consider fetching this from the request)
        BigDecimal interestRate = calculateInterestRate(loanTermMonths);

        BigDecimal totalInterest = loanRequest.getAmount().multiply(interestRate).divide(BigDecimal.valueOf(100));
        BigDecimal finalAmount = loanRequest.getAmount().add(totalInterest);
        BigDecimal monthlyInstallment = finalAmount.divide(BigDecimal.valueOf(loanTermMonths), RoundingMode.HALF_UP);

        Loan loan = new Loan(loanRequest.getAccountNumber(), loanRequest.getAmount(), interestRate, loanTermMonths);
        loan.setMonthlyInstallment(monthlyInstallment);
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

        Optional<Customer> customerOpt = customerRepository.findById(loanRequest.getAccountNumber());
        emailService.sendEmail(customerOpt.get().getEmail(), "Your loan request " + loanRequestId + " has been approved.", "Loan Approval");

        return "Loan successfully approved and credited for account: " + loanRequest.getAccountNumber();
    }

    private BigDecimal calculateInterestRate(int months) {
        if (months <= 12) {
            return BigDecimal.valueOf(5);
        } else if (months <= 24) {
            return BigDecimal.valueOf(6);
        } else if (months <= 36) {
            return BigDecimal.valueOf(7);
        } else {
            return BigDecimal.valueOf(8);
        }
    }

    public String repayLoan(String loanId, BigDecimal repaymentAmount) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);

        if (loanOpt.isEmpty()) {
            return "No active loan found with this ID.";
        }

        Loan loan = loanOpt.get();
        if (!loan.isActive()) {
            return "Loan is already settled.";
        }

        Optional<Account> accountOpt = accountRepository.findById(loan.getAccountNumber());
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
        transaction.setAccountNumber(loan.getAccountNumber());
        transaction.setType("Loan_Debit");
        transaction.setAmount(actualRepaymentAmount);
        transactionRepository.save(transaction);

        account.setBalance(account.getBalance().subtract(actualRepaymentAmount));
        account.getTransactions().add(transaction);
        accountRepository.save(account);

        remainingBalance = remainingBalance.subtract(actualRepaymentAmount);

        Optional<Customer> customerOpt = customerRepository.findById(loan.getAccountNumber());

        emailService.sendEmail(customerOpt.get().getEmail(), "Loan repayment of INR " + actualRepaymentAmount + " completed.", "Loan Repayment");

        if (remainingBalance.compareTo(BigDecimal.ZERO) <= 0) {
            loan.setRemainingBalance(BigDecimal.ZERO);
            loan.setActive(false);
            loanRepository.save(loan);

            emailService.sendEmail(customerOpt.get().getEmail(), "Loan fully repaid. No remaining balance.", "Loan Repayment Completion");
            return "Loan fully repaid. No remaining balance.";
        }

        loan.setRemainingBalance(remainingBalance);
        loanRepository.save(loan);

        return "Loan repayment successful. Remaining Balance: " + remainingBalance;
    }

    public String processMonthlyInstallments() {
        Iterable<Loan> activeLoans = loanRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Loan loan : activeLoans) {
            if (!loan.isActive() || !loan.getNextInstallmentDate().isEqual(today)) {
                continue;
            }

            BigDecimal monthlyInterestRate = loan.getInterestRate().divide(MONTHS_IN_YEAR, RoundingMode.HALF_UP);
            BigDecimal numerator = loan.getLoanAmount().multiply(monthlyInterestRate).multiply((BigDecimal.ONE.add(monthlyInterestRate)).pow(loan.getLoanTermMonths()));
            BigDecimal denominator = ((BigDecimal.ONE.add(monthlyInterestRate)).pow(loan.getLoanTermMonths())).subtract(BigDecimal.ONE);
            BigDecimal monthlyInstallment = numerator.divide(denominator, RoundingMode.HALF_UP);

            repayLoan(loan.getLoanId(), monthlyInstallment);

            loan.setNextInstallmentDate(loan.getNextInstallmentDate().plusMonths(1));
            loanRepository.save(loan);
        }

        return "Monthly installments processed successfully.";
    }


    public String getLoanStatus(String loanId) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isEmpty()) {
            return "No loan found with this ID.";
        }
        Loan loan = loanOpt.get();
        return "Loan Status: Remaining Balance: " + loan.getRemainingBalance() + ", Due Date: " + loan.getDueDate();
    }
}