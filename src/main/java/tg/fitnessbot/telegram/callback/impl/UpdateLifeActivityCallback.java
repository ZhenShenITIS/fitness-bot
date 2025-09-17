package tg.fitnessbot.telegram.callback.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.LifeActivity;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.UserService;
import tg.fitnessbot.telegram.callback.Callback;
import tg.fitnessbot.utils.MessageUtil;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateLifeActivityCallback implements Callback {
    CallbackName callbackName = CallbackName.UPDATE_LIFE_ACTIVITY;

    @Autowired
    TelegramConfig telegramConfig;

    @Autowired
    UserService userService;

    @Autowired
    MessageUtil messageUtil;

    // TODO Применить к этом методу паттерн абстрактный метод, текст сообщения можно в отдельный метод вынести
    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery, SpringWebhookBot springWebhookBot) {
        int messageId = callbackQuery.getMessage().getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();
        long allowId = Long.parseLong(callbackQuery.getData().split(":")[1]);
        long userId = callbackQuery.getFrom().getId();
        if (allowId == userId) {
            List<KeyboardButton> list = new ArrayList<>();
            EditMessageText editMessageText = EditMessageText
                    .builder()
                    .messageId(messageId)
                    .chatId(chatId)
                    .text(MessageText.REQUEST_LIFE_ACTIVITY.getMessageText())
                    .build();
            telegramConfig.getUserStateMap().put(callbackQuery.getFrom().getId(), CallbackName.UPDATE_LIFE_ACTIVITY);

            return editMessageText;
        }
        return null;
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message, SpringWebhookBot springWebhookBot) {
        // TODO Сделать реплей маркап и вынести строки в файл
        UserForm user = userService.getUserByID(message.getFrom().getId());
        if (message.getText().toLowerCase().equals("1")) {
            user.setLifeActivity(LifeActivity.MIN);
        } else if (message.getText().toLowerCase().equals("2")) {
            user.setLifeActivity(LifeActivity.LIGHT);
        } else if (message.getText().toLowerCase().equals("3")) {
            user.setLifeActivity(LifeActivity.MID);
        } else if (message.getText().toLowerCase().equals("4")) {
            user.setLifeActivity(LifeActivity.HIGH);
        } else if (message.getText().toLowerCase().equals("5")) {
            user.setLifeActivity(LifeActivity.VERY_HIGH);
        } else {
            return SendMessage.builder().chatId(message.getChatId()).text(MessageText.WRONG_LIFE_ACTIVITY.getMessageText()).build();
        }
        userService.updateUser(user);
        telegramConfig.getUserStateMap().put(user.getId(), CallbackName.NONE);
        return messageUtil.getEditProfileMessage(message, springWebhookBot);
    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
