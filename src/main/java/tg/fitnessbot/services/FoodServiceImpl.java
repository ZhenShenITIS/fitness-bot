package tg.fitnessbot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tg.fitnessbot.dto.FoodForm;
import tg.fitnessbot.models.Food;
import tg.fitnessbot.repositories.FoodRepository;

@Component
public class FoodServiceImpl implements FoodService {
    @Autowired
    FoodRepository foodRepository;

    @Override
    public boolean addFood(FoodForm form) {
        Food exFood = foodRepository.findByName(form.getName());
        if (exFood == null) {
            Food food = Food
                    .builder()
                    .name(form.getName())
                    .kcal(form.getKcal())
                    .protein(form.getProtein())
                    .carbohydrates(form.getCarbohydrates())
                    .fat(form.getFat())
                    .build();
            foodRepository.save(food);
            return true;
        }
        return false;
    }
}
