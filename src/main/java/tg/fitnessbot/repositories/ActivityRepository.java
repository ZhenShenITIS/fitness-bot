package tg.fitnessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tg.fitnessbot.models.Activity;
import tg.fitnessbot.models.Food;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findByName(String name);

    List<Activity> findNamesByNameIsNot(String name);
}
