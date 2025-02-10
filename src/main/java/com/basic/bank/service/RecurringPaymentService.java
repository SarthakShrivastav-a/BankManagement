package com.basic.bank.service;

import com.basic.bank.entity.Account;
import com.basic.bank.entity.RecurringPayment;
import com.basic.bank.entity.Transaction;
import com.basic.bank.repository.AccountRepository;
import com.basic.bank.repository.RecurringPaymentRepository;
import com.basic.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RecurringPaymentService {

    @Autowired
    private RecurringPaymentRepository recurringPaymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public RecurringPayment createRecurringPayment(String accountId, String serviceName, BigDecimal amount, int frequencyInDays, String notes) {
//        if (amount.compareTo(BigDecimal.valueOf(100)) < 0) {
//            throw new IllegalArgumentException("Minimum recurring payment amount is 100.");
//        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Not enough balance to set up recurring payment.");
        }

        RecurringPayment payment = new RecurringPayment(
                null, accountId, serviceName, amount.doubleValue(), LocalDate.now(), frequencyInDays, false, notes);

        payment = recurringPaymentRepository.save(payment);
        account.getRecurringPayments().add(payment);
        accountRepository.save(account);

        return payment;
    }

    @Transactional
    public void processRecurringPayments() {
        List<RecurringPayment> payments = recurringPaymentRepository.findAll();
        LocalDate today = LocalDate.now();

        for (RecurringPayment payment : payments) {
            if (!payment.isPaused() && today.minusDays(payment.getFrequencyInDays()).isAfter(payment.getStartDate())) {
                Account account = accountRepository.findById(payment.getAccountNumber())
                        .orElseThrow(() -> new IllegalArgumentException("Account not found."));

                if (account.getBalance().compareTo(BigDecimal.valueOf(payment.getAmount())) >= 0) {
                    account.setBalance(account.getBalance().subtract(BigDecimal.valueOf(payment.getAmount())));

                    Transaction transaction = new Transaction();
                    transaction.setAccountNumber(payment.getAccountNumber());
                    transaction.setType("RECURRING_PAYMENT");
                    transaction.setAmount(BigDecimal.valueOf(payment.getAmount()));

                    transactionRepository.save(transaction);
                    account.getTransactions().add(transaction);
                    accountRepository.save(account);

                    payment.setStartDate(today);
                    recurringPaymentRepository.save(payment);
                }
            }
        }
    }

    @Transactional
    public RecurringPayment pauseRecurringPayment(String paymentId) {
        RecurringPayment payment = recurringPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Recurring payment not found."));

        payment.setPaused(true);
        return recurringPaymentRepository.save(payment);
    }

    @Transactional
    public RecurringPayment resumeRecurringPayment(String paymentId) {
        RecurringPayment payment = recurringPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Recurring payment not found."));

        payment.setPaused(false);
        return recurringPaymentRepository.save(payment);
    }

    @Transactional
    public void cancelRecurringPayment(String paymentId) {
        RecurringPayment payment = recurringPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Recurring payment not found."));

        Account account = accountRepository.findById(payment.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));

        account.getRecurringPayments().remove(payment);
        accountRepository.save(account);
        recurringPaymentRepository.deleteById(paymentId);
    }

    public List<RecurringPayment> getRecurringPaymentsByAccount(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));

        return account.getRecurringPayments();
    }

    public RecurringPayment getRecurringPaymentsById(String id){
        RecurringPayment recurringPayment = recurringPaymentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Recurring Payment with that id not found"));
        return recurringPayment;
    }
}
