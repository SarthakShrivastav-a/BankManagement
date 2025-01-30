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

//    @Transactional
    public String deposit(String accountNumber, BigDecimal amount){
        Optional<Account> accountOptional = accountRepository.findById(accountNumber);
        if(accountOptional.isPresent()){
            Account customer = accountOptional.get();
            customer.setBalance((customer.getBalance().add(amount)));
            //now update thee transaction data tooooo
            Transaction transaction = new Transaction();
            transaction.setAccountNumber(accountNumber);
            transaction.setType("DEPOSIT");
            transaction.setAmount(amount);
            transactionRepository.save(transaction);
            if (customer.getTransactions() == null) {
                customer.setTransactions(new ArrayList<>());
            }
            customer.getTransactions().add(transaction);
            accountRepository.save(customer);
            return "Transaction Completed Succesfully "+customer.getBalance();
        }
        return "User With That Account Number Not Found";
    }

    public String withdraw(String accountNumber, BigDecimal amount){
        Optional<Account> customerOptional = accountRepository.findById(accountNumber);
        if(customerOptional.isPresent()){
            Account customer = customerOptional.get();
            if(customer.getBalance().compareTo(amount)>0) {
                customer.setBalance((customer.getBalance().subtract(amount)));
                //now update thee transaction data tooooo
                Transaction transaction = new Transaction();
                transaction.setAccountNumber(accountNumber);
                transaction.setType("WITHDRAW");
                transaction.setAmount(amount);
                transactionRepository.save(transaction);
                if (customer.getTransactions() == null) {
                    customer.setTransactions(new ArrayList<>());
                }
                customer.getTransactions().add(transaction);
                accountRepository.save(customer);
                return "Transaction Completed Succesfully " + customer.getBalance();
            }
            return "Not Sufficient Balance";
        }
        return "User With That Account Number Not Found";
    }
    public String transfer(String accountNumber, BigDecimal amount,String receiverNumber){
        Optional<Account> senderOpt = accountRepository.findById(accountNumber);
        Optional<Account> receiverOpt = accountRepository.findById(receiverNumber);
        if(senderOpt.isPresent()&& receiverOpt.isPresent()){
            Account sender = senderOpt.get();
            Account receiver = receiverOpt.get();
            if(sender.getBalance().compareTo(amount)>0){
                sender.setBalance(sender.getBalance().subtract(amount));
                receiver.setBalance(receiver.getBalance().add(amount));
                //transaction Update
                //sender
                Transaction sendTransaction = new Transaction();
                sendTransaction.setAccountNumber(accountNumber);
                sendTransaction.setType("TRANSFER");
                sendTransaction.setAmount(amount);
                sendTransaction.setReceiverAccount(receiverNumber);
                transactionRepository.save(sendTransaction);
                if (receiver.getTransactions() == null) {
                    receiver.setTransactions(new ArrayList<>());
                }
                sender.getTransactions().add(sendTransaction);
                receiver.getTransactions().add(sendTransaction);
                accountRepository.save(sender);
                accountRepository.save(receiver);
                return "Transfer Succesfully Initiated and Completed";
            }
            return "Account Low On Balance";
        }
        return "Receiver or Sender account Not Found!";
    }

    public Transaction getTransactionById(String id){
        Optional<Transaction> transaction =  transactionRepository.findById(id);
        if (transaction.isPresent()){
            return transaction.get();
        }
        return null;
    }

}
