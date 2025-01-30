package com.basic.bank.service;

import com.basic.bank.entity.Customer;
import com.basic.bank.entity.Transaction;
import com.basic.bank.repository.CustomerRepository;
import com.basic.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

//    @Transactional
    public String deposit(String accountNumber, BigDecimal amount){
        Optional<Customer> customerOptional = customerRepository.findById(accountNumber);
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            customer.setBalance((customer.getBalance().add(amount)));
            //now update thee transaction data tooooo
            Transaction transaction = new Transaction();
            transaction.setAccountNumber(accountNumber);
            transaction.setType("DEPOSIT");
            transaction.setAmount(amount);
            transactionRepository.save(transaction);
//            if (customer.getTransactions() == null) {
//                customer.setTransactions(new ArrayList<>());
//            }
            customer.getTransactions().add(transaction);
            customerRepository.save(customer);
            return "Transaction Completed Succesfully "+customer.getBalance();
        }
        return "User With That Account Number Not Found";
    }

    public String withdraw(String accountNumber, BigDecimal amount){
        Optional<Customer> customerOptional = customerRepository.findById(accountNumber);
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();
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
                customerRepository.save(customer);
                return "Transaction Completed Succesfully " + customer.getBalance();
            }
            return "Not Sufficient Balance";
        }
        return "User With That Account Number Not Found";
    }
    public String transfer(String accountNumber, BigDecimal amount,String receiverNumber){
        Optional<Customer> senderOpt = customerRepository.findById(accountNumber);
        Optional<Customer> receiverOpt = customerRepository.findById(receiverNumber);
        if(senderOpt.isPresent()&& receiverOpt.isPresent()){
            Customer sender = senderOpt.get();
            Customer receiver = receiverOpt.get();
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
                customerRepository.save(sender);
                customerRepository.save(receiver);
                return "Transfer Succesfully Initiated and Completed";
            }
            return "Account Low On Balance";
        }
        return "Receiver or Sender aaccount Not Found!";
    }
}
