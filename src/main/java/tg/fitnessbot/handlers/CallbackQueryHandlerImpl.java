package tg.fitnessbot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tg.fitnessbot.callback.CallbackContainer;

@Component
public class CallbackQueryHandlerImpl implements CallbackQueryHandler {
    private final CallbackContainer callbackContainer;

    public CallbackQueryHandlerImpl(CallbackContainer callbackContainer) {
        this.callbackContainer = callbackContainer;
    }

    @Override
    public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery, Long userId) {
        String callbackIdentifier = callbackQuery.getData();
        return callbackContainer.retrieveCallback(callbackIdentifier).processCallback(callbackQuery, userId);
    }
}
