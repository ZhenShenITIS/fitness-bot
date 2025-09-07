package tg.fitnessbot.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Component
public interface CallbackQueryHandler {
    BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery, SpringWebhookBot springWebhookBot);
}
