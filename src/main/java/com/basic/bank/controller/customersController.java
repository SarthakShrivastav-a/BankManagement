package com.basic.bank.controller;


import com.basic.bank.entity.Customer;
import com.basic.bank.entity.SignUp;
import com.basic.bank.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class customersController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SignUp> createCustomer(@Valid @RequestBody SignUp signUp) {
        customerService.addCustomer(signUp);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return new ResponseEntity<>(customerService.getCustomers(), HttpStatus.ACCEPTED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Customer> updateUser(@RequestBody Customer customer, @RequestParam String account) {
        return new ResponseEntity<>(customerService.updateUser(account, customer), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCustomer(@RequestParam String account) {
        String response = customerService.deleteCustomer(account);
        return new  ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }

}
