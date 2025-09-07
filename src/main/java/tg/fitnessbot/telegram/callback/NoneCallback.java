package tg.fitnessbot.telegram.callback;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.constants.CallbackName;

@Component
public class NoneCallback implements Callback {
    CallbackName callbackName = CallbackName.NONE;
    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery, SpringWebhookBot springWebhookBot) {
        return null;
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message, SpringWebhookBot springWebhookBot) {
        return null;
    }
    @Override
    public CallbackName getCallback(){
        return callbackName;
    }
}
