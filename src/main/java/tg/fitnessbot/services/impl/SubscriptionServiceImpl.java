package tg.fitnessbot.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tg.fitnessbot.constants.IntegerConstants;
import tg.fitnessbot.models.Subscription;
import tg.fitnessbot.models.TrialAttempts;
import tg.fitnessbot.models.User;
import tg.fitnessbot.repositories.SubscriptionRepository;
import tg.fitnessbot.repositories.TrialAttemptsRepository;
import tg.fitnessbot.repositories.UserRepository;
import tg.fitnessbot.services.SubscriptionService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    SubscriptionRepository subscriptionRepository;

    TrialAttemptsRepository trialAttemptsRepository;

    UserRepository userRepository;

    @Override
    public void subscribe(Long userId, int days) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return;
        }
        Subscription actual = getActualSubscribe(user);
        if (actual == null) {
            LocalDate now = LocalDate.now();
            Subscription subscription = Subscription
                    .builder()
                    .countOfSubscriptionDays(days)
                    .startDay(now)
                    .user(user)
                    .build();
            subscriptionRepository.save(subscription);
        } else {
            int newDays = actual.getCountOfSubscriptionDays() + days;
            actual.setCountOfSubscriptionDays(newDays);
            subscriptionRepository.save(actual);
        }
    }

    @Override
    public boolean canUserUseBasicPremiumOption(Long userId) {
        if (hasSubscription(userId)) {
            return true;
        }
        return hasTrial(userId);

    }

    @Override
    public void useTrialIfNotSubscribed(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return;
        }
        Subscription actual = getActualSubscribe(user);

        if (actual == null) {
            TrialAttempts trialAttempts = getTrialAttemptsInstance(user);
            trialAttempts.setRemainingAttempts(trialAttempts.getRemainingAttempts() - 1);
            trialAttemptsRepository.save(trialAttempts);
        }
    }

    @Override
    public int getDayOfPremium(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return 0;
        }

        Subscription actual = getActualSubscribe(user);
        if (actual == null) {
            return 0;
        } else return (int) ChronoUnit.DAYS.between(LocalDate.now(),
                actual.getStartDay()
                        .plusDays(actual
                                .getCountOfSubscriptionDays()));


    }

    @Override
    public int getTrialAttempts(Long userId) {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return 0;
        }

        return getTrialAttemptsInstance(user).getRemainingAttempts();
    }

    private boolean hasTrial (Long userId) {
        return getTrialAttempts(userId) > 0;
    }

    private boolean hasSubscription (Long userId) {
        return getDayOfPremium(userId) > 0;
    }

    private Subscription getActualSubscribe (User user) {

        List<Subscription> subscriptions = subscriptionRepository.findByUser(user);
        if (subscriptions.isEmpty()) {
            return null;
        }

        LocalDate now = LocalDate.now();

        for (Subscription sub : subscriptions) {
            LocalDate dateEndOfSub = sub.getStartDay().plusDays(sub.getCountOfSubscriptionDays());
            if (ChronoUnit.DAYS.between(now, dateEndOfSub) > 0) {
                return sub;
            }
        }
        return null;
    }

    private TrialAttempts getTrialAttemptsInstance (User user) {
        TrialAttempts trialAttempts = trialAttemptsRepository.findByUser(user);

        if (trialAttempts == null) {
            TrialAttempts newTrialAttempts = TrialAttempts
                    .builder()
                    .remainingAttempts(IntegerConstants.COUNT_OF_TRIAL_ATTEMPTS.getValue())
                    .user(user)
                    .build();
            trialAttemptsRepository.save(newTrialAttempts);
            return newTrialAttempts;
        } else return trialAttempts;
    }
}
