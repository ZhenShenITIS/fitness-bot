package tg.fitnessbot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class CallbackQueryHandlerImpl implements CallbackQueryHandler {
    @Override
    public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
        return null;
    }
}
