package com.basic.bank.repository;

import com.basic.bank.entity.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends MongoRepository<Loan, String> {
    Optional<Loan> findById(String loanId);
}