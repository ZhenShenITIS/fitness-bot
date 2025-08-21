package tg.fitnessbot.services;

import tg.fitnessbot.dto.ActivityForm;
import tg.fitnessbot.dto.FoodForm;
import tg.fitnessbot.models.Activity;

import java.util.HashMap;

public interface ActivityService {

    boolean addActivity(ActivityForm form);

    boolean deleteActivity(ActivityForm form);

    boolean updateActivity(ActivityForm form);

    ActivityForm getActivityByName(String name);

    // TODO Попробовать переписать на обычную мапу, как будто плохая идея использовать классы
    HashMap<ActivityForm, Double> getActivityByName (HashMap<String, Double> activities);

    Double calculateActivity (HashMap<ActivityForm, Double> activities, double weight);


}
