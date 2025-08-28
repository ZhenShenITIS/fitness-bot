package tg.fitnessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tg.fitnessbot.models.Food;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    Food findByName(String name);

    @Query(value = "select name from food", nativeQuery = true)
    List<Food> findAll();
}
