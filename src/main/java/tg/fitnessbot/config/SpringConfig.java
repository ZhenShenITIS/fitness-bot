package tg.fitnessbot.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import tg.fitnessbot.command.AddFoodCommand;
import tg.fitnessbot.command.CommandContainer;
import tg.fitnessbot.command.StartCommand;
import tg.fitnessbot.handlers.UpdateHandler;

@Configuration
@AllArgsConstructor
public class SpringConfig {
    private final TelegramConfig telegramConfig;
    @Autowired
    StartCommand startCommand;
    @Autowired
    AddFoodCommand addFoodCommand;
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

    @Bean
    public CommandContainer commandContainer() {
        return new CommandContainer(startCommand, addFoodCommand);
    }
}