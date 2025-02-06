package com.basic.bank.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = "loans")
public class Loan {
    @Id
    private String accountNumber;

    private BigDecimal loanAmount;
    private BigDecimal remainingBalance;
    private BigDecimal interestRate;
    private LocalDate loanStartDate;
    private LocalDate dueDate;
    private boolean isActive;

    public Loan() {}

    public Loan(String accountNumber, BigDecimal loanAmount, BigDecimal interestRate) {
        this.accountNumber = accountNumber;
        this.loanAmount = loanAmount;
        this.remainingBalance = loanAmount;
        this.interestRate = interestRate;
        this.loanStartDate = LocalDate.now();
        this.dueDate = loanStartDate.plusMonths(12); // Example term: 12 months
        this.isActive = true;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getLoanStartDate() {
        return loanStartDate;
    }

    public void setLoanStartDate(LocalDate loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}