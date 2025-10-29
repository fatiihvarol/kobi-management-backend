package com.example.schedulerservice.scheduler;

import com.example.schedulerservice.model.Subscription;
import com.example.schedulerservice.service.SubscriptionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SubscriptionRenewalScheduler {

    private final SubscriptionService subscriptionService;

    public SubscriptionRenewalScheduler(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // This job runs every day at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void renewDueSubscriptions() {
        System.out.println("Running renewDueSubscriptions job at " + LocalDate.now());
        LocalDate today = LocalDate.now();
        List<Subscription> dueSubscriptions = subscriptionService.getDueSubscriptions(today);

        if (dueSubscriptions.isEmpty()) {
            System.out.println("No subscriptions due for renewal today.");
            return;
        }

        System.out.println(dueSubscriptions.size() + " subscriptions found due for renewal.");
        for (Subscription subscription : dueSubscriptions) {
            subscriptionService.renewSubscription(subscription);
        }
        System.out.println("Finished renewDueSubscriptions job.");
    }
}
