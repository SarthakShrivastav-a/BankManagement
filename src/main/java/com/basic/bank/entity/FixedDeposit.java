package com.basic.bank.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

@Document(collection = "fixedDeposits")
public class FixedDeposit {

    private String accountNumber;
    private BigDecimal depositAmount;
    private double interestRate;
    private LocalDate startDate;
    private LocalDate maturityDate;
    private BigDecimal maturityAmount;
    private String status;

    public FixedDeposit() {}

    public FixedDeposit(String accountNumber, BigDecimal depositAmount, LocalDate startDate, LocalDate maturityDate) {
        this.accountNumber = accountNumber;
        this.depositAmount = depositAmount;
        this.startDate = startDate;
        this.maturityDate = maturityDate;
        this.interestRate = calculateInterestRate();
        this.maturityAmount = calculateMaturityAmount();
        this.status = "ACTIVE";
    }

    private double calculateInterestRate() {
        long months = java.time.temporal.ChronoUnit.MONTHS.between(startDate, maturityDate);

        if (months <= 12) {
            return 5.0; // 5% for 12 months or less
        } else if (months <= 24) {
            return 5.75; // 5.75% for 13 to 24 months
        } else if (months <= 36) {
            return 6.5; // 6.5% for 25 to 36 months
        } else {
            return 7.5; // 7.5% for more than 36 months
        }
    }

    private BigDecimal calculateMaturityAmount() {
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, maturityDate);
        double interest = (depositAmount.doubleValue() * interestRate * days) / 36500;
        return depositAmount.add(BigDecimal.valueOf(interest));
    }
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public BigDecimal getMaturityAmount() {
        return maturityAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

