package tg.fitnessbot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tg.fitnessbot.callback.*;
import tg.fitnessbot.command.AddFoodCommand;
import tg.fitnessbot.command.Command;
import tg.fitnessbot.command.CommandContainer;
import tg.fitnessbot.command.StartCommand;

@Configuration
public class ContainersConfig {
    @Autowired
    StartCommand startCommand;
    @Autowired
    AddFoodCommand addFoodCommand;
    @Bean
    public CommandContainer commandContainer() {
        // TODO Подумать над тем, как можно сделать более красивое создание экземпляра контейнера
        return new CommandContainer(new Command[]{startCommand, addFoodCommand});
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
