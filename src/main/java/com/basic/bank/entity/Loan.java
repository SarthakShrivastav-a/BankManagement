package com.basic.bank.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Document(collection = "loans")
public class Loan {
    @Id
    private String loanId; // Unique Loan Identifier

    private String accountNumber; // Foreign key reference to Account
    private BigDecimal loanAmount;
    private BigDecimal remainingBalance;
    private BigDecimal interestRate;
    private LocalDate loanStartDate;
    private LocalDate dueDate;
    private boolean isActive;

    public Loan() {}

    public Loan(String accountNumber, BigDecimal loanAmount, BigDecimal interestRate) {
        this.loanId = UUID.randomUUID().toString(); // Generate Unique Loan ID
        this.accountNumber = accountNumber;
        this.loanAmount = loanAmount;
        this.remainingBalance = loanAmount;
        this.interestRate = interestRate;
        this.loanStartDate = LocalDate.now();
        this.dueDate = loanStartDate.plusMonths(12); // Example: 12 months term
        this.isActive = true;
    }

    public String getLoanId() {
        return loanId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public LocalDate getLoanStartDate() {
        return loanStartDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
