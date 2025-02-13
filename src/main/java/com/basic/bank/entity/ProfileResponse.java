package com.basic.bank.entity;

import com.basic.bank.entity.Customer;
import com.basic.bank.entity.Transaction;
import java.math.BigDecimal;
import java.util.List;

public class ProfileResponse {
    private Customer customerDetails;
    private List<Transaction> transactionHistory;
    private BigDecimal balance;
//    private List<Transaction> statements;

    public ProfileResponse(Customer customerDetails, List<Transaction> transactionHistory, BigDecimal balance) {
        this.customerDetails = customerDetails;
        this.transactionHistory = transactionHistory;
        this.balance = balance;
//        this.statements = statements;
    }

    public Customer getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(Customer customerDetails) {
        this.customerDetails = customerDetails;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(List<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

//    public List<Transaction> getStatements() {
//        return statements;
//    }
//
//    public void setStatements(List<Transaction> statements) {
//        this.statements = statements;
//    }
}

