package tg.fitnessbot.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import tg.fitnessbot.telegram.handlers.bot.UpdateHandler;

@Configuration
@AllArgsConstructor
@EnableAsync
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
}