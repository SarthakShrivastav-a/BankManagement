package com.basic.bank.repository;

import com.basic.bank.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account,String> {

    Account findByaccountNumber(String accountNumber);
}
