package com.basic.bank.service;

import com.basic.bank.entity.Customer;
import com.basic.bank.entity.Transaction;
import com.basic.bank.repository.CustomerRepository;
import com.basic.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

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
            return "Transaction Completed Succesfully"+customer.getBalance();

        }
        return "User With That Account Number Not Found";
    }
}
