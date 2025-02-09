//package com.basic.bank.service;
//
//import com.basic.bank.entity.Account;
//import com.basic.bank.entity.Customer;
//import com.basic.bank.entity.Transaction;
//import com.basic.bank.repository.AccountRepository;
//import com.basic.bank.repository.CustomerRepository;
//import com.basic.bank.repository.TransactionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//@Service
//public class TransactionService {
//
//    private static final BigDecimal SAVINGS_DAILY_LIMIT = new BigDecimal("50000");
//    private static final BigDecimal CURRENT_DAILY_LIMIT = new BigDecimal("100000");
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private EmailService emailService;
//
//    private void checkIfAccountBlocked(Account account) {
//        if (account.isBlocked()) {
//            throw new IllegalStateException("Your account is blocked. No transactions are allowed.");
//        }
//    }
//
//    private String getEmailByAccountNumber(String accountNumber) {
//        Optional<Customer> customerOpt = customerRepository.findById(accountNumber);
//        return customerOpt.map(Customer::getEmail).orElse(null);
//    }
//
//    private void sendTransactionEmail(String accountNumber, String transactionType, BigDecimal amount, BigDecimal balance) {
//        String email = getEmailByAccountNumber(accountNumber);
//        if (email != null) {
//            String subject = "Bank Transaction Notification";
//            String body = String.format("Dear Customer,\n\nYour %s of ₹%s was successful. Your updated balance is ₹%s.\n\nThank you for banking with us!",
//                    transactionType, amount, balance);
//            emailService.sendEmail(email, body, subject);
//        }
//    }
//
//    @Transactional
//    public String deposit(String accountNumber, BigDecimal amount) {
//        Optional<Account> accountOptional = accountRepository.findById(accountNumber);
//        if (accountOptional.isPresent()) {
//            Account customer = accountOptional.get();
//            try {
//                checkIfAccountBlocked(customer);
//                customer.setBalance(customer.getBalance().add(amount));
//                Transaction transaction = new Transaction();
//                transaction.setAccountNumber(accountNumber);
//                transaction.setType("DEPOSIT");
//                transaction.setAmount(amount);
//                transactionRepository.save(transaction);
//                customer.getTransactions().add(transaction);
//                accountRepository.save(customer);
//
//                sendTransactionEmail(accountNumber, "deposit", amount, customer.getBalance());
//
//                return "Transaction completed successfully. Balance: " + customer.getBalance();
//            } catch (IllegalStateException e) {
//                return e.getMessage();
//            }
//        }
//        return "User with that account number not found.";
//    }
//
//    @Transactional
//    public String withdraw(String accountNumber, BigDecimal amount) {
//        Optional<Account> accountOptional = accountRepository.findById(accountNumber);
//        if (accountOptional.isPresent()) {
//            Account customer = accountOptional.get();
//            try {
//                checkIfAccountBlocked(customer);
//                switch (customer.getAccountType()) {
//                    case "FIXED":
//                        return "Transaction cannot be completed on a fixed deposit account.";
//                    case "SAVINGS":
//                        if (amount.compareTo(SAVINGS_DAILY_LIMIT) > 0) {
//                            return "Cannot withdraw more than ₹50,000 in a single day from a savings account.";
//                        }
//                        break;
//                    case "CURRENT":
//                        if (amount.compareTo(CURRENT_DAILY_LIMIT) > 0) {
//                            return "Cannot withdraw more than ₹100,000 in a single day from a current account.";
//                        }
//                        break;
//                }
//
//                if (customer.getBalance().compareTo(amount) >= 0) {
//                    customer.setBalance(customer.getBalance().subtract(amount));
//                    Transaction transaction = new Transaction();
//                    transaction.setAccountNumber(accountNumber);
//                    transaction.setType("WITHDRAW");
//                    transaction.setAmount(amount);
//                    transactionRepository.save(transaction);
//                    customer.getTransactions().add(transaction);
//                    accountRepository.save(customer);
//
//                    sendTransactionEmail(accountNumber, "withdrawal", amount, customer.getBalance());
//
//                    return "Transaction completed successfully. Balance: " + customer.getBalance();
//                } else {
//                    return "Not sufficient balance.";
//                }
//            } catch (IllegalStateException e) {
//                return e.getMessage();
//            }
//        }
//        return "User with that account number not found.";
//    }
//
//    public String transfer(String accountNumber, BigDecimal amount, String receiverNumber) {
//        Optional<Account> senderOpt = accountRepository.findById(accountNumber);
//        Optional<Account> receiverOpt = accountRepository.findById(receiverNumber);
//        if (senderOpt.isPresent() && receiverOpt.isPresent()) {
//            Account sender = senderOpt.get();
//            Account receiver = receiverOpt.get();
//            try {
//                checkIfAccountBlocked(sender);
//                if (sender.getBalance().compareTo(amount) > 0) {
//                    sender.setBalance(sender.getBalance().subtract(amount));
//                    receiver.setBalance(receiver.getBalance().add(amount));
//                    Transaction sendTransaction = new Transaction();
//                    sendTransaction.setAccountNumber(accountNumber);
//                    sendTransaction.setType("TRANSFER");
//                    sendTransaction.setAmount(amount);
//                    sendTransaction.setReceiverAccount(receiverNumber);
//                    transactionRepository.save(sendTransaction);
//                    sender.getTransactions().add(sendTransaction);
//                    receiver.getTransactions().add(sendTransaction);
//                    accountRepository.save(sender);
//                    accountRepository.save(receiver);
//
//                    sendTransactionEmail(accountNumber, "transfer", amount, sender.getBalance());
//
//                    return "Transfer successfully initiated and completed.";
//                }
//                return "Account low on balance.";
//            } catch (IllegalStateException e) {
//                return e.getMessage();
//            }
//        }
//        return "Receiver or sender account not found.";
//    }
//}
package com.basic.bank.service;

import com.basic.bank.entity.Account;
import com.basic.bank.entity.Customer;
import com.basic.bank.entity.Transaction;
import com.basic.bank.repository.AccountRepository;
import com.basic.bank.repository.CustomerRepository;
import com.basic.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {

    private static final BigDecimal SAVINGS_DAILY_LIMIT = new BigDecimal("50000");
    private static final BigDecimal CURRENT_DAILY_LIMIT = new BigDecimal("100000");

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PdfService pdfService;

    private void checkIfAccountBlocked(Account account) {
        if (account.isBlocked()) {
            throw new IllegalStateException("Your account is blocked. No transactions are allowed.");
        }
    }

    private String getEmailByAccountNumber(String accountNumber) {
        Optional<Customer> customerOpt = customerRepository.findById(accountNumber);
        return customerOpt.map(Customer::getEmail).orElse(null);
    }

    private void sendTransactionEmail(String accountNumber, String transactionType, BigDecimal amount, BigDecimal balance, byte[] pdfReceipt) {
        String email = getEmailByAccountNumber(accountNumber);
        if (email != null) {
            String subject = "Bank Transaction Notification";
            String body = String.format("Dear Customer,\n\nYour %s of ₹%s was successful. Your updated balance is ₹%s.\n\nThank you for banking with us!", transactionType, amount, balance);
            emailService.sendEmailWithAttachment(email, body, subject, pdfReceipt, "Transaction_Receipt.pdf");
        }
    }

    @Transactional
    public String deposit(String accountNumber, BigDecimal amount) {
        Optional<Account> accountOptional = accountRepository.findById(accountNumber);
        if (accountOptional.isPresent()) {
            Account customer = accountOptional.get();
            try {
                checkIfAccountBlocked(customer);
                customer.setBalance(customer.getBalance().add(amount));
                Transaction transaction = new Transaction();
                transaction.setAccountNumber(accountNumber);
                transaction.setType("DEPOSIT");
                transaction.setAmount(amount);
                transactionRepository.save(transaction);
                customer.getTransactions().add(transaction);
                accountRepository.save(customer);

                byte[] pdfReceipt = pdfService.generateTransactionDepositReceipt(accountNumber,amount);
                sendTransactionEmail(accountNumber, "deposit", amount, customer.getBalance(), pdfReceipt);

                return "Transaction completed successfully. Balance: " + customer.getBalance();
            } catch (IllegalStateException e) {
                return e.getMessage();
            }
        }
        return "User with that account number not found.";
    }

    @Transactional
    public String withdraw(String accountNumber, BigDecimal amount) {
        Optional<Account> accountOptional = accountRepository.findById(accountNumber);
        if (accountOptional.isPresent()) {
            Account customer = accountOptional.get();
            try {
                checkIfAccountBlocked(customer);
                if ((customer.getAccountType().equals("SAVINGS") && amount.compareTo(SAVINGS_DAILY_LIMIT) > 0) ||
                        (customer.getAccountType().equals("CURRENT") && amount.compareTo(CURRENT_DAILY_LIMIT) > 0)) {
                    return "Withdrawal limit exceeded.";
                }

                if (customer.getBalance().compareTo(amount) >= 0) {
                    customer.setBalance(customer.getBalance().subtract(amount));
//                    Transaction transaction = new Transaction(accountNumber, "WITHDRAW", amount);
                Transaction transaction = new Transaction();
                transaction.setAccountNumber(accountNumber);
                transaction.setType("WITHDRAW");
                transaction.setAmount(amount);
                    transactionRepository.save(transaction);
                    customer.getTransactions().add(transaction);
                    accountRepository.save(customer);

                    byte[] pdfReceipt = pdfService.generateTransactionWithdrawReceipt(accountNumber,amount);
                    sendTransactionEmail(accountNumber, "withdrawal", amount, customer.getBalance(), pdfReceipt);

                    return "Transaction completed successfully. Balance: " + customer.getBalance();
                } else {
                    return "Not sufficient balance.";
                }
            } catch (IllegalStateException e) {
                return e.getMessage();
            }
        }
        return "User with that account number not found.";
    }

    @Transactional
    public String transfer(String accountNumber, BigDecimal amount, String receiverNumber,String message) {
        Optional<Account> senderOpt = accountRepository.findById(accountNumber);
        Optional<Account> receiverOpt = accountRepository.findById(receiverNumber);
        if (senderOpt.isPresent() && receiverOpt.isPresent()) {
            Account sender = senderOpt.get();
            Account receiver = receiverOpt.get();
            try {
                checkIfAccountBlocked(sender);
                if (sender.getBalance().compareTo(amount) > 0) {
                    sender.setBalance(sender.getBalance().subtract(amount));
                    receiver.setBalance(receiver.getBalance().add(amount));
                Transaction transaction = new Transaction();
                transaction.setAccountNumber(accountNumber);
                transaction.setType("TRANSFER");
                transaction.setAmount(amount);
                transaction.setReceiverAccount(receiverNumber);
                transaction.setMessage(message);
                    transactionRepository.save(transaction);
                    sender.getTransactions().add(transaction);
                    receiver.getTransactions().add(transaction);
                    accountRepository.save(sender);
                    accountRepository.save(receiver);

                    byte[] pdfReceipt = pdfService.generateTransactionTransferReceipt(accountNumber,receiverNumber,amount);
                    sendTransactionEmail(accountNumber, "transfer", amount, sender.getBalance(), pdfReceipt);

                    return "Transfer successfully initiated and completed.";
                }
                return "Account low on balance.";
            } catch (IllegalStateException e) {
                return e.getMessage();
            }
        }
        return "Receiver or sender account not found.";
    }
}
