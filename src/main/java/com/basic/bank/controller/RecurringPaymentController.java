package com.basic.bank.controller;

import com.basic.bank.entity.RecurringPayment;
import com.basic.bank.service.RecurringPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/recurring-payments")
public class RecurringPaymentController {

    @Autowired
    private RecurringPaymentService recurringPaymentService;

    @PostMapping("/create")
    public ResponseEntity<RecurringPayment> createRecurringPayment(
            @RequestParam String accountId,
            @RequestParam String serviceName,
            @RequestParam BigDecimal amount,
            @RequestParam int frequencyInDays,
            @RequestParam(required = false) String notes) {

        RecurringPayment payment = recurringPaymentService.createRecurringPayment(
                accountId, serviceName, amount, frequencyInDays, notes);

        return ResponseEntity.ok(payment);
    }

    //automate this shit with @Scheduled
    @PostMapping("/process")
    public ResponseEntity<String> processRecurringPayments() {
        recurringPaymentService.processRecurringPayments();
        return ResponseEntity.ok("Recurring payments processed successfully.");
    }

    @PutMapping("/pause/{paymentId}")
    public ResponseEntity<RecurringPayment> pauseRecurringPayment(@PathVariable String paymentId) {
        return ResponseEntity.ok(recurringPaymentService.pauseRecurringPayment(paymentId));
    }

    @PutMapping("/resume/{paymentId}")
    public ResponseEntity<RecurringPayment> resumeRecurringPayment(@PathVariable String paymentId) {
        return ResponseEntity.ok(recurringPaymentService.resumeRecurringPayment(paymentId));
    }

    @DeleteMapping("/cancel/{paymentId}")
    public ResponseEntity<String> cancelRecurringPayment(@PathVariable String paymentId) {
        recurringPaymentService.cancelRecurringPayment(paymentId);
        return ResponseEntity.ok("Recurring payment canceled successfully.");
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<RecurringPayment>> getRecurringPaymentsByAccount(@PathVariable String accountId) {
        return ResponseEntity.ok(recurringPaymentService.getRecurringPaymentsByAccount(accountId));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<RecurringPayment> getRecurringPaymentById(@PathVariable String paymentId) {
        return ResponseEntity.ok(recurringPaymentService.getRecurringPaymentsById(paymentId));
    }
}
