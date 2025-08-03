package tg.fitnessbot.services;

import tg.fitnessbot.dto.FoodForm;

import java.util.HashMap;
import java.util.List;

public interface FoodService {
    boolean addFood(FoodForm form);

    FoodForm getFoodByName(String name);

    FoodForm calculateFood (HashMap<String, Double> foods);
}
