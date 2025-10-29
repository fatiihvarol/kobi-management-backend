package com.example.paymentservice.bank;

import com.example.paymentservice.model.Payment;
import com.example.paymentservice.model.Transaction;

public interface BankIntegrationService {
    Transaction processPayment(Payment payment);
    // Other bank-specific operations like refund, void, etc.
}
