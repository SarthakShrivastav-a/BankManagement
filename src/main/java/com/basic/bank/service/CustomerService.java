package com.basic.bank.service;

import com.basic.bank.entity.AuthUser;
import com.basic.bank.entity.Customer;
import com.basic.bank.entity.SignUp;
import com.basic.bank.repository.AuthUserRepository;
import com.basic.bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private CustomerRepository customerRepository;


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


//    @Transactional
    public void addCustomer(SignUp signUp){
        String accNumber = generateAccountNumber();
        String passWord = encoder.encode(signUp.getPassword());
        AuthUser authUser = new AuthUser(accNumber, signUp.getEmail(),passWord,"CUSTOMER");
        Customer customer = new Customer(accNumber, signUp.getName(), signUp.getEmail(), signUp.getPhoneNumber());
        authUserRepository.save(authUser);
        customerRepository.save(customer);
    }
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }
    public Optional<Customer> getCustomer(String accountNum){
        return customerRepository.findById(accountNum);
    }
    public static String generateAccountNumber() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
    }
}
