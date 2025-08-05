package tg.fitnessbot.services;

import tg.fitnessbot.dto.FoodForm;

import java.util.HashMap;

public interface FoodService {
    boolean addFood(FoodForm form);

    FoodForm getFoodByName(String name);

    HashMap<FoodForm, Double> getFoodByName (HashMap<String, Double> foods);

    FoodForm calculateFood (HashMap<FoodForm, Double> foods);
}
