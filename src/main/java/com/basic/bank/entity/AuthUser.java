package com.basic.bank.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "AuthUser")
@Data

public class AuthUser {

    @Id
    private String id;

    private String email;

    @JsonIgnore
    private String hashedPassword;

    private String role;

    public AuthUser(String id,String email,String hashedPassword,String role){
        this.id = id;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }



}
