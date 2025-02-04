package com.basic.bank.service;

import com.basic.bank.entity.Account;
import com.basic.bank.entity.Transaction;
import com.basic.bank.repository.AccountRepository;
import com.basic.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    private void checkIfAccountBlocked(Account account) {
        if (account.isBlocked()) {
            throw new IllegalStateException("Your account is blocked. No transactions are allowed.");
        }
    }

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
                return "Transaction completed successfully. Balance: " + customer.getBalance();
            } catch (IllegalStateException e) {
                return e.getMessage();
            }
        }
        return "User with that account number not found.";
    }

    public String withdraw(String accountNumber, BigDecimal amount) {
        Optional<Account> accountOptional = accountRepository.findById(accountNumber);
        if (accountOptional.isPresent()) {
            Account customer = accountOptional.get();
            try {
                checkIfAccountBlocked(customer);
                if (customer.getBalance().compareTo(amount) > 0) {
                    customer.setBalance(customer.getBalance().subtract(amount));
                    Transaction transaction = new Transaction();
                    transaction.setAccountNumber(accountNumber);
                    transaction.setType("WITHDRAW");
                    transaction.setAmount(amount);
                    transactionRepository.save(transaction);
                    customer.getTransactions().add(transaction);
                    accountRepository.save(customer);
                    return "Transaction completed successfully. Balance: " + customer.getBalance();
                }
                return "Not sufficient balance.";
            } catch (IllegalStateException e) {
                return e.getMessage();
            }
        }
        return "User with that account number not found.";
    }

    public String transfer(String accountNumber, BigDecimal amount, String receiverNumber) {
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
                    Transaction sendTransaction = new Transaction();
                    sendTransaction.setAccountNumber(accountNumber);
                    sendTransaction.setType("TRANSFER");
                    sendTransaction.setAmount(amount);
                    sendTransaction.setReceiverAccount(receiverNumber);
                    transactionRepository.save(sendTransaction);
                    sender.getTransactions().add(sendTransaction);
                    receiver.getTransactions().add(sendTransaction);
                    accountRepository.save(sender);
                    accountRepository.save(receiver);
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
