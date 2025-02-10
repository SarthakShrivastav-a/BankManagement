package com.basic.bank.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "recurring_payments")
public class RecurringPayment {

    @Id
    private String paymentId;

    private String accountNumber;

    private String serviceName;

    private double amount;

    private LocalDate startDate;

    private int frequencyInDays;

    private boolean isPaused;

    private String notes;

    public RecurringPayment(String paymentId, String accountId, String serviceName, double amount,
                            LocalDate startDate, int frequencyInDays, boolean isPaused, String notes) {
        this.paymentId = paymentId;
        this.accountNumber = accountId;
        this.serviceName = serviceName;
        this.amount = amount;
        this.startDate = startDate;
        this.frequencyInDays = frequencyInDays;
        this.isPaused = isPaused;
        this.notes = notes;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getFrequencyInDays() {
        return frequencyInDays;
    }

    public void setFrequencyInDays(int frequencyInDays) {
        this.frequencyInDays = frequencyInDays;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
