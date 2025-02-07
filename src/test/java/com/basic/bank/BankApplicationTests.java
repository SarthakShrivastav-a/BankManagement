package com.basic.bank;

import com.basic.bank.entity.Customer;
import com.basic.bank.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BankApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	CustomerService customerService;

	@Test
	public void getCustomers(){
		customerService.getCustomers();
	}

}
