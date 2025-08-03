package tg.fitnessbot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tg.fitnessbot.dto.FoodForm;
import tg.fitnessbot.models.Food;
import tg.fitnessbot.repositories.FoodRepository;

import java.util.HashMap;
import java.util.List;

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
                    .name(form.getName().toLowerCase())
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

    @Override
    public FoodForm getFoodByName(String name) {
        Food food = foodRepository.findByName(name);
        if (food != null) {
            return FoodForm
                    .builder()
                    .name(food.getName())
                    .kcal(food.getKcal())
                    .protein(food.getProtein())
                    .fat(food.getFat())
                    .carbohydrates(food.getCarbohydrates())
                    .build();
        }
        else {
            return null;
        }
    }

    @Override
    public FoodForm calculateFood (HashMap<String, Double> foods) {
        double kcal = 0;
        double protein = 0;
        double fat = 0;
        double carbohydrates = 0;
        for (String key : foods.keySet()) {
            FoodForm foodForm = getFoodByName(key);
            if (foodForm != null) {
                kcal += (foodForm.getKcal() / 100) * foods.get(key);
                protein += (foodForm.getProtein() / 100) * foods.get(key);
                fat += (foodForm.getFat() / 100) * foods.get(key);
                carbohydrates += (foodForm.getCarbohydrates() / 100) * foods.get(key);
            } else {
                throw new NullPointerException();
            }
        }
        return FoodForm
                .builder()
                .kcal(kcal)
                .protein(protein)
                .fat(fat)
                .carbohydrates(carbohydrates)
                .build();
    }
}
