package tg.fitnessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.fitnessbot.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
