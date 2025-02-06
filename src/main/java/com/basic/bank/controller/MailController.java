package com.basic.bank.controller;

import com.basic.bank.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    EmailService emailService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void sendMail(@RequestParam String recipient,@RequestParam String body,@RequestParam String subject) {
        emailService.sendEmail(recipient, body, subject);
    }
}
