package com.basic.bank.repository;

import com.basic.bank.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByAccountNumberAndTimestampBetween(
            String accountNumber, LocalDateTime start, LocalDateTime end);
}
