package tg.fitnessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.fitnessbot.models.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
