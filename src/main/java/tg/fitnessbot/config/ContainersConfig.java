package tg.fitnessbot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tg.fitnessbot.callback.*;
import tg.fitnessbot.command.*;

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

    @Bean
    public CallbackContainer callbackContainer() {
        return new CallbackContainer(new Callback[]{updateBirthdayCallback, updateGenderCallback, updateHeightCallback, updateWeightCallback, noneCallback});
    }
}
