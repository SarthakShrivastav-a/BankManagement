package com.basic.bank.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "loans")
public class Loan {
    @Id
    private String loanId; // Unique Loan Identifier

    private String accountNumber; // Foreign key reference to Account
    private BigDecimal loanAmount;
    private BigDecimal remainingBalance;
    private BigDecimal interestRate;
    private int loanTermMonths; // Loan term in months
    private LocalDate loanStartDate;
    private LocalDate dueDate;
    private boolean isActive;
    private LocalDate nextInstallmentDate;

    @DBRef
    private List<Transaction> loanInstallments;

    public Loan() {}

    public Loan(String accountNumber, BigDecimal loanAmount, BigDecimal interestRate, int loanTermMonths) {
        this.loanId = UUID.randomUUID().toString(); // Generate Unique Loan ID
        this.accountNumber = accountNumber;
        this.loanAmount = loanAmount;
        this.remainingBalance = loanAmount;
        this.interestRate = interestRate;
        this.loanTermMonths = loanTermMonths;
        this.loanStartDate = LocalDate.now();
        this.dueDate = loanStartDate.plusMonths(loanTermMonths); // Set due date
        this.isActive = true;
        this.nextInstallmentDate = loanStartDate.plusMonths(1); // First installment after 1 month
        this.loanInstallments = new ArrayList<>();
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

    public int getLoanTermMonths() {
        return loanTermMonths;
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

    public LocalDate getNextInstallmentDate() {
        return nextInstallmentDate;
    }

    public void setNextInstallmentDate(LocalDate nextInstallmentDate) {
        this.nextInstallmentDate = nextInstallmentDate;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setLoanInstallments(List<Transaction> loanInstallments) {
        this.loanInstallments = loanInstallments;
    }
}