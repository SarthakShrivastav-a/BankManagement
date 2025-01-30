package com.basic.bank.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "customers")
public class Customer {

    @Id
    private final String accountNumber;
    private String name;
    private String email;
    private String phoneNumber;
    private BigDecimal balance;

    @DBRef
    private List<Transaction> transactions;

    public Customer(String accountNumber, String name, String email, String phoneNumber) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.balance = BigDecimal.ZERO;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

//    public void setAccountNumber(String accountNumber) {
//        if (accountNumber == null) {
//            throw new IllegalArgumentException("Account number cannot be null");
//        }
//        this.accountNumber = accountNumber;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        if (balance == null) {
            throw new IllegalArgumentException("Balance cannot be null");
        }
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = new ArrayList<>();
    }
}
