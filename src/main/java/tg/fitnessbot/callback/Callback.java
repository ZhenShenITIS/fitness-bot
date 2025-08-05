package tg.fitnessbot.callback;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Callback {
    BotApiMethod<?> processCallback (CallbackQuery callbackQuery);

    BotApiMethod<?> answerMessage(Message message);
}
