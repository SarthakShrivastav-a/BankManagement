package com.basic.bank.service;

import com.basic.bank.entity.*;
import com.basic.bank.repository.AccountRepository;
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

    @Autowired
    private AccountRepository accountRepository;


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


//    @Transactional
    public void addCustomer(SignUp signUp){
        String accNumber = generateAccountNumber();
        String passWord = encoder.encode(signUp.getPassword());
        AuthUser authUser = new AuthUser(accNumber, signUp.getEmail(),passWord,"CUSTOMER");
        Customer customer = new Customer(accNumber, signUp.getName(), signUp.getEmail(), signUp.getPhoneNumber());
        Account account = new Account(accNumber);
        authUserRepository.save(authUser);
        customerRepository.save(customer);
        accountRepository.save(account);
    }
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }
    public Optional<Customer> getCustomer(String accountNum){
        return customerRepository.findById(accountNum);
    }

    public List<Transaction> getHistory(String accountNumber){
        Optional<Account> customer = accountRepository.findById(accountNumber);
        if(customer.isPresent()){
            return customer.get().getTransactions();
        }
        return null;
    }
    public Customer updateUser(String account,Customer customer){
        Optional<Customer> customerOpt = customerRepository.findById(account);
        if (customerOpt.isPresent()){
            Customer updatedCustomer = customerOpt.get();
            if (customer.getName() != null) {
                updatedCustomer.setName(customer.getName());
            }
            if (customer.getEmail() != null) {
                updatedCustomer.setEmail(customer.getEmail());
            }
            if (customer.getPhoneNumber() != null) {
                updatedCustomer.setPhoneNumber(customer.getPhoneNumber());
            }

            customerRepository.save(updatedCustomer);
            return updatedCustomer;
        }
        return null;
    }






//    public List<>
    public static String generateAccountNumber() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
    }
}
