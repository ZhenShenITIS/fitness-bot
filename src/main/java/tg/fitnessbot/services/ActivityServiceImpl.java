package tg.fitnessbot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tg.fitnessbot.dto.ActivityForm;
import tg.fitnessbot.models.Activity;
import tg.fitnessbot.repositories.ActivityRepository;

import java.util.HashMap;
@Component
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityRepository activityRepository;

    @Override
    public boolean addActivity(ActivityForm form) {
        Activity exActivity = activityRepository.findByName(form.getName());
        if (exActivity == null) {
            Activity activity = Activity.builder().name(form.getName()).met(form.getMet()).build();
            activityRepository.save(activity);
            return true;
        }
        return false;

    }

    @Override
    public boolean deleteActivity(ActivityForm form) {
        Activity exActivity = activityRepository.findByName(form.getName());
        if (exActivity != null) {
            activityRepository.delete(exActivity);
            return true;
        }
        return false;

    }

    @Override
    public boolean updateActivity(ActivityForm form) {
        Activity exActivity = activityRepository.findByName(form.getName());
        if (exActivity != null) {
            exActivity.setName(form.getName());
            exActivity.setMet(form.getMet());
            activityRepository.save(exActivity);
        }
        return false;

    }

    @Override
    public ActivityForm getActivityByName(String name) {
        Activity activity = activityRepository.findByName(name);
        if (activity != null) {
            return ActivityForm.builder().met(activity.getMet()).name(activity.getName()).build();
        }
        return null;
    }

    @Override
    public HashMap<ActivityForm, Double> getFoodByName(HashMap<String, Double> activities) {
         HashMap<ActivityForm, Double> map = new HashMap<>();
         for (String nameOfActivity : activities.keySet()) {
             ActivityForm form = getActivityByName(nameOfActivity);
             if (form != null) {
                 map.put(form, activities.get(nameOfActivity));
             }

         }
         return map;
    }

    @Override
    public Double calculateActivity(HashMap<ActivityForm, Double> activities, double weight) {
        Double kcal = 0.0;
        for (ActivityForm form : activities.keySet()) {
            kcal += activities.get(form)/60 * form.getMet() * weight;
        }
        return kcal;

    }
}
