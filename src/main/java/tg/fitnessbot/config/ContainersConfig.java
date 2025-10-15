package tg.fitnessbot.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tg.fitnessbot.telegram.callback.*;
import tg.fitnessbot.telegram.callback.impl.*;
import tg.fitnessbot.telegram.command.*;
import tg.fitnessbot.telegram.command.impl.*;
import tg.fitnessbot.telegram.containers.CallbackContainer;
import tg.fitnessbot.telegram.containers.CommandContainer;

@AllArgsConstructor
@Configuration
public class ContainersConfig {
    StartCommand startCommand;
    AddFoodCommand addFoodCommand;
    DeleteFoodCommand deleteFoodCommand;
    UpdateFoodCommand updateFoodCommand;
    AddActivityCommand addActivityCommand;
    DeleteActivityCommand deleteActivityCommand;
    UpdateActivityCommand updateActivityCommand;
    CalculateActivityCommand calculateActivityCommand;
    HelpCommand helpCommand;
    CalculateFoodCommand calculateFoodCommand;
    SubscriptionCommand subscriptionCommand;

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
                helpCommand,
                subscriptionCommand
        });
    }


    UpdateBirthdayCallback updateBirthdayCallback;
    UpdateGenderCallback updateGenderCallback;
    UpdateHeightCallback updateHeightCallback;
    UpdateWeightCallback updateWeightCallback;
    NoneCallback noneCallback;
    UpdateLifeActivityCallback updateLifeActivityCallback;
    UpdatePhotoCallback updatePhotoCallback;
    EditProfileCallback editProfileCallback;
    BackToProfileCallback backToProfileCallback;
    BasicSubscriptionCallback basicSubscriptionCallback;
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
                editProfileCallback,
                backToProfileCallback,
                basicSubscriptionCallback
        });
    }
}
