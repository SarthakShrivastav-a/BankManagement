package com.basic.bank.repository;

import com.basic.bank.entity.LoanRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoanRequestRepository extends MongoRepository<LoanRequest,String> {
}
