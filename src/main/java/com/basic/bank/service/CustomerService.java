package com.basic.bank.service;

import com.basic.bank.entity.*;
import com.basic.bank.repository.AccountRepository;
import com.basic.bank.repository.AuthUserRepository;
import com.basic.bank.repository.CustomerRepository;
import com.basic.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Autowired
    private TransactionRepository transactionRepository;


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    @Transactional
    public void addCustomer(SignUp signUp){
        String accNumber = generateAccountNumber();
        String passWord = encoder.encode(signUp.getPassword());
        AuthUser authUser = new AuthUser(accNumber, signUp.getEmail(),passWord,"CUSTOMER");
        Customer customer = new Customer(accNumber, signUp.getName(), signUp.getEmail(), signUp.getPhoneNumber(), signUp.getAddress(), signUp.getCitizenship(), signUp.getOccupation());
        Account account = new Account(accNumber,signUp.getAccountType());
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
    @Transactional
    public Customer updateUser(String account,Customer customer){
        Optional<Customer> customerOpt = customerRepository.findById(account);
        Optional<AuthUser> AuthOpt = authUserRepository.findById(account);
        Optional<Account> accountOptional = accountRepository.findById(account);
        if (customerOpt.isPresent()){
            Customer updatedCustomer = customerOpt.get();
            AuthUser authUser = AuthOpt.get();
            if (customer.getName() != null) {
                updatedCustomer.setName(customer.getName());
            }
            if (customer.getEmail() != null) {
                updatedCustomer.setEmail(customer.getEmail());
                authUser.setEmail(customer.getEmail());
            }
            if (customer.getPhoneNumber() != null) {
                updatedCustomer.setPhoneNumber(customer.getPhoneNumber());
            }
            if (customer.getAddress() != null) {
                updatedCustomer.setAddress(customer.getAddress());
            }
            if (customer.getOccupation() != null) {
                updatedCustomer.setOccupation(customer.getOccupation());
            }
            if (customer.getCitizenship() != null) {
                updatedCustomer.setCitizenship(customer.getCitizenship());
            }

            customerRepository.save(updatedCustomer);
            authUserRepository.save(authUser);
            return updatedCustomer;
        }
        return null;
    }
    @Transactional
    public String deleteCustomer(String account){
        if (customerRepository.existsById(account)){
            customerRepository.deleteById(account);
            accountRepository.deleteById(account);
            authUserRepository.deleteById(account);
            return "User Deleted Successfully";
        }
        return "User Not Found";
    }

    public BigDecimal getBalance(String accountNumber) {
        Optional<Account> accountOptional = accountRepository.findById(accountNumber);
        if (accountOptional.isPresent()) {
            return accountOptional.get().getBalance();
        }
        return null;
    }

    public List<Transaction> getStatements(String accountNumber, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByAccountNumberAndTimestampBetween(
                accountNumber,
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );
    }






//    public List<>
    public static String generateAccountNumber() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
    }
}
