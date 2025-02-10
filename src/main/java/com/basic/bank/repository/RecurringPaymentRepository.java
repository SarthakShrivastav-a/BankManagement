package com.basic.bank.repository;

import com.basic.bank.entity.RecurringPayment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecurringPaymentRepository extends MongoRepository<RecurringPayment,String> {
}
