package tg.fitnessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tg.fitnessbot.models.Food;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    Food findByName(String name);

    // TODO Узнать как правильно получать столбцы из бд
    List<Food> findNamesByNameIsNot(String name);
}
