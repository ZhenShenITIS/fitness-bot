package tg.fitnessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.fitnessbot.models.TrialAttempts;
import tg.fitnessbot.models.User;

public interface TrialAttemptsRepository extends JpaRepository<TrialAttempts, Long> {
    TrialAttempts findByUser(User user);

}
