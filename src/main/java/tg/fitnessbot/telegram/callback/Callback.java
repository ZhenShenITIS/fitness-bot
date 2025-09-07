package tg.fitnessbot.telegram.callback;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.constants.CallbackName;

public interface Callback {
    BotApiMethod<?> processCallback (CallbackQuery callbackQuery, SpringWebhookBot springWebhookBot);

    BotApiMethod<?> answerMessage(Message message, SpringWebhookBot springWebhookBot);
    CallbackName getCallback();
}
