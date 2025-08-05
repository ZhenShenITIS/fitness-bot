package tg.fitnessbot.callback;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class UpdateBirthdayCallback implements Callback {
    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery, Long userId) {
//        callbackQuery.getFrom().getId()
        return null;
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message) {
        return null;
    }
}
