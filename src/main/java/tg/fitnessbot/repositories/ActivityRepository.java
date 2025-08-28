package tg.fitnessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tg.fitnessbot.models.Activity;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findByName(String name);

    @Query(value = "select name from activity", nativeQuery = true)
    List<Activity> findAll();
}
