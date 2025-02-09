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

    private String address;
    private String citizenship;
    private String occupation;

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Customer(String accountNumber, String name, String email, String phoneNumber,String address,String citizenship,String occupation) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.citizenship=citizenship;
        this.occupation=occupation;
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


}
