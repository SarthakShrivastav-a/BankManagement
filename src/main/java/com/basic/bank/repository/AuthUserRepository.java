package com.basic.bank.repository;

import com.basic.bank.entity.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthUserRepository extends MongoRepository<AuthUser,String> {
}
