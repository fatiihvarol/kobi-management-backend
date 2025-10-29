package com.example.schedulerservice.service;

import com.example.schedulerservice.model.Subscription;
import com.example.schedulerservice.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<Subscription> getDueSubscriptions(LocalDate date) {
        return subscriptionRepository.findByEndDateBeforeAndActiveTrue(date);
    }

    public Subscription renewSubscription(Subscription subscription) {
        // In a real application, this would involve more complex logic:
        // 1. Payment processing
        // 2. Updating subscription period (e.g., extend endDate by a month/year)
        // 3. Potentially sending renewal confirmation email
        System.out.println("Renewing subscription for user: " + subscription.getUserId() + " with plan: " + subscription.getPlanType());
        subscription.setEndDate(subscription.getEndDate().plusMonths(1)); // Example: extend by one month
        return subscriptionRepository.save(subscription);
    }

    public Subscription saveSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }
}
