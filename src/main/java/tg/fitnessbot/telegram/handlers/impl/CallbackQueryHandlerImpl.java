package tg.fitnessbot.telegram.handlers.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.telegram.callback.impl.CallbackContainer;
import tg.fitnessbot.telegram.handlers.CallbackQueryHandler;

@Component
public class CallbackQueryHandlerImpl implements CallbackQueryHandler {

    private final CallbackContainer callbackContainer;

    public CallbackQueryHandlerImpl(CallbackContainer callbackContainer) {
        this.callbackContainer = callbackContainer;
    }

    @Override
    public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery, SpringWebhookBot springWebhookBot) {
        String callbackIdentifier = callbackQuery.getData().split(":")[0];
        return callbackContainer.retrieveCallback(callbackIdentifier).processCallback(callbackQuery, springWebhookBot);
    }
}
