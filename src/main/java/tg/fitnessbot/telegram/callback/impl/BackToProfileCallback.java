package tg.fitnessbot.telegram.callback.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.telegram.callback.Callback;
import tg.fitnessbot.utils.MessageUtil;

@Component
@RequiredArgsConstructor
public class BackToProfileCallback implements Callback {
    CallbackName callbackName = CallbackName.BACK_TO_PROFILE;

    MessageUtil messageUtil;

    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery, SpringWebhookBot springWebhookBot) {
        long allowId = Long.parseLong(callbackQuery.getData().split(":")[1]);
        long userId = callbackQuery.getFrom().getId();
        if (allowId == userId) {
            return messageUtil.getProfileMessage(callbackQuery, springWebhookBot);
        }
        return null;

    }

    @Override
    public BotApiMethod<?> answerMessage(Message message, SpringWebhookBot springWebhookBot) {
        return null;
    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
