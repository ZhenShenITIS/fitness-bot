package tg.fitnessbot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public interface CallbackQueryHandler {
    BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery);
}
