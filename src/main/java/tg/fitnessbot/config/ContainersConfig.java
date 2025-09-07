package tg.fitnessbot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tg.fitnessbot.telegram.callback.*;
import tg.fitnessbot.telegram.command.*;

@Configuration
public class ContainersConfig {
    @Autowired
    StartCommand startCommand;
    @Autowired
    AddFoodCommand addFoodCommand;
    @Autowired
    DeleteFoodCommand deleteFoodCommand;
    @Autowired
    UpdateFoodCommand updateFoodCommand;
    @Autowired
    AddActivityCommand addActivityCommand;
    @Autowired
    DeleteActivityCommand deleteActivityCommand;
    @Autowired
    UpdateActivityCommand updateActivityCommand;
    @Autowired
    CalculateActivityCommand calculateActivityCommand;
    @Autowired
    HelpCommand helpCommand;
    @Autowired
    CalculateFoodCommand calculateFoodCommand;
    @Bean
    public CommandContainer commandContainer() {
        // TODO Подумать над тем, как можно сделать более красивое создание экземпляра контейнера
        return new CommandContainer(new Command[]{
                startCommand,
                addFoodCommand,
                deleteFoodCommand,
                updateFoodCommand,
                addActivityCommand,
                deleteActivityCommand,
                updateActivityCommand,
                calculateActivityCommand,
                calculateFoodCommand,
                helpCommand
        });
    }


    @Autowired
    UpdateBirthdayCallback updateBirthdayCallback;
    @Autowired
    UpdateGenderCallback updateGenderCallback;
    @Autowired
    UpdateHeightCallback updateHeightCallback;
    @Autowired
    UpdateWeightCallback updateWeightCallback;
    @Autowired
    NoneCallback noneCallback;
    @Autowired
    UpdateLifeActivityCallback updateLifeActivityCallback;
    @Autowired
    UpdatePhotoCallback updatePhotoCallback;
    @Autowired
    EditProfileCallback editProfileCallback;

    @Bean
    public CallbackContainer callbackContainer() {
        return new CallbackContainer(new Callback[]{
                updateBirthdayCallback,
                updateGenderCallback,
                updateHeightCallback,
                updateWeightCallback,
                noneCallback,
                updateLifeActivityCallback,
                updatePhotoCallback,
                editProfileCallback
        });
    }
}
