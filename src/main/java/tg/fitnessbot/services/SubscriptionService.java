package tg.fitnessbot.services;

public interface SubscriptionService {
    void subscribe(Long userId, int days);

    boolean canUserUseBasicPremiumOption (Long userId);

    void useTrialIfNotSubscribed (Long userId);

    int getDayOfPremium (Long userId);

    int getTrialAttempts (Long userId);
}
