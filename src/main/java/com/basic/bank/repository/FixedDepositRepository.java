package com.basic.bank.repository;

import com.basic.bank.entity.FixedDeposit;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FixedDepositRepository extends MongoRepository<FixedDeposit,String> {
}
