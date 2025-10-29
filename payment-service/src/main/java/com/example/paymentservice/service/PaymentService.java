package com.example.paymentservice.service;

import com.example.paymentservice.bank.BankIntegrationService;
import com.example.paymentservice.model.Payment;
import com.example.paymentservice.model.Transaction;
import com.example.paymentservice.repository.PaymentRepository;
import com.example.paymentservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final BankIntegrationService garantiBankService;
    private final BankIntegrationService yapiKrediBankService;

    public PaymentService(PaymentRepository paymentRepository,
                          TransactionRepository transactionRepository,
                          @Qualifier("garantiBankService") BankIntegrationService garantiBankService,
                          @Qualifier("yapiKrediBankService") BankIntegrationService yapiKrediBankService) {
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
        this.garantiBankService = garantiBankService;
        this.yapiKrediBankService = yapiKrediBankService;
    }

    public Payment processPayment(Payment payment, String preferredBank) {
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("PENDING");
        Payment savedPayment = paymentRepository.save(payment);

        BankIntegrationService bankService;
        if ("GARANTI".equalsIgnoreCase(preferredBank)) {
            bankService = garantiBankService;
        } else if ("YAPIKREDI".equalsIgnoreCase(preferredBank)) {
            bankService = yapiKrediBankService;
        } else {
            // Default or error handling
            throw new IllegalArgumentException("Unsupported bank: " + preferredBank);
        }

        Transaction transaction = bankService.processPayment(savedPayment);
        transactionRepository.save(transaction);

        savedPayment.setStatus(transaction.getStatus());
        savedPayment.setBankReferenceId(transaction.getBankTransactionId());
        return paymentRepository.save(savedPayment);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }
}
