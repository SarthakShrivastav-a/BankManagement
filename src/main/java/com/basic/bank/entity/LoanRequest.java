package com.basic.bank.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "loanRequests")
public class LoanRequest {

    @Id
    private String id;

    private String accountNumber;

    private BigDecimal amount;

    private String status; // PENDING, APPROVED, or REJECTED

    public LoanRequest() {
    }

    public LoanRequest(String accountNumber, BigDecimal amount, String status) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LoanRequest{" +
                "id='" + id + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
