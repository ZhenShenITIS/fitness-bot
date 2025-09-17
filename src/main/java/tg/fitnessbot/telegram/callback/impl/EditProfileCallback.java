package tg.fitnessbot.telegram.callback.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.telegram.callback.Callback;
import tg.fitnessbot.utils.MessageUtil;

@Component
public class EditProfileCallback implements Callback {
    CallbackName callbackName = CallbackName.EDIT_PROFILE;
    @Autowired
    TelegramConfig telegramConfig;

    @Autowired
    MessageUtil messageUtil;

    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery, SpringWebhookBot springWebhookBot) {
        int messageId = callbackQuery.getMessage().getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();
        long allowId = Long.parseLong(callbackQuery.getData().split(":")[1]);
        long userId = callbackQuery.getFrom().getId();
        if (allowId == userId) {

            telegramConfig.getUserStateMap().put(callbackQuery.getFrom().getId(), CallbackName.EDIT_PROFILE);

            return messageUtil.getEditProfileMessage(callbackQuery, springWebhookBot);
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
