package tg.fitnessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.fitnessbot.models.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findByName(String name);
}
