package tg.fitnessbot.callback;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.constants.CallbackName;

@Component
public class NoneCallback implements Callback {
    CallbackName callbackName = CallbackName.NONE;
    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery) {
        return null;
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message) {
        return null;
    }
    @Override
    public CallbackName getCallback(){
        return callbackName;
    }
}
