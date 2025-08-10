package tg.fitnessbot.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import tg.fitnessbot.callback.*;
import tg.fitnessbot.command.AddFoodCommand;
import tg.fitnessbot.command.Command;
import tg.fitnessbot.command.CommandContainer;
import tg.fitnessbot.command.StartCommand;
import tg.fitnessbot.handlers.UpdateHandler;

@Configuration
@AllArgsConstructor
public class SpringConfig {
    private final TelegramConfig telegramConfig;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramConfig.getWebhookPath()).build();
    }

    @Bean
    public UpdateHandler springWebhookBot(SetWebhook setWebhook) {
        UpdateHandler bot = new UpdateHandler(setWebhook);
        bot.setBotPath(telegramConfig.getWebhookPath());
        bot.setBotUsername(telegramConfig.getBotName());
        bot.setBotToken(telegramConfig.getBotToken());
        return bot;
    }

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