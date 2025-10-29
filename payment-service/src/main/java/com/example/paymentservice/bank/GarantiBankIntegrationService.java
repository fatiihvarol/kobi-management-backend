package com.example.paymentservice.bank;

import com.example.paymentservice.model.Payment;
import com.example.paymentservice.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service("garantiBankService")
public class GarantiBankIntegrationService implements BankIntegrationService {

    @Override
    public Transaction processPayment(Payment payment) {
        // Simulate integration with Garanti Bank API
        System.out.println("Processing payment with Garanti Bank for amount: " + payment.getAmount());

        Transaction transaction = new Transaction();
        transaction.setPaymentId(payment.getId().toString());
        transaction.setBankName("Garanti Bank");
        transaction.setTransactionType("AUTHORIZE_CAPTURE");
        transaction.setAmount(payment.getAmount());
        transaction.setCurrency(payment.getCurrency());
        transaction.setTransactionDate(LocalDateTime.now());

        // Simulate success or failure
        if (payment.getAmount().doubleValue() > 0) { // Simple condition for success
            transaction.setStatus("SUCCESS");
            transaction.setBankTransactionId(UUID.randomUUID().toString());
            transaction.setResponseCode("00");
            transaction.setResponseMessage("Approved");
        } else {
            transaction.setStatus("FAILED");
            transaction.setResponseCode("99");
            transaction.setResponseMessage("Declined by Garanti");
        }
        return transaction;
    }
}
