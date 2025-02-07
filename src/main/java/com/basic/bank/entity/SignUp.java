package com.basic.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


//@Data
@JsonIgnoreProperties(ignoreUnknown = false)  // this will cause an error if unknown fields are sent
public class SignUp {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Account type is required CURRENT SAVING FIXED")
    private String accountType;

    public @NotBlank(message = "Account type is required CURRENT SAVING FIXED") String getAccountType() {
        return accountType;
    }

    public void setAccountType(@NotBlank(message = "Account type is required CURRENT SAVING FIXED") String accountType) {
        this.accountType = accountType;
    }

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
