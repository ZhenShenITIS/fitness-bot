package tg.fitnessbot.telegram.callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.UserService;
import tg.fitnessbot.utils.MessageUtil;

@Component
public class UpdateHeightCallback implements Callback {
    CallbackName callbackName = CallbackName.UPDATE_HEIGHT;

    @Autowired
    UserService userService;

    @Autowired
    TelegramConfig telegramConfig;

    @Autowired
    MessageUtil messageUtil;

    @Override
    public CallbackName getCallback(){
        return callbackName;
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message, SpringWebhookBot springWebhookBot) {
        UserForm user = userService.getUserByID(message.getFrom().getId());
        try {
            user.setHeight(Integer.parseInt(message.getText().replaceAll(",",".")));
        } catch (NumberFormatException e) {
            return SendMessage.builder().chatId(message.getChatId()).text(MessageText.WRONG_HEIGHT.getMessageText()).build();
        }
        userService.updateUser(user);
        telegramConfig.getUserStateMap().put(user.getId(), CallbackName.NONE);
        try {
            springWebhookBot.execute(SendMessage.builder().chatId(message.getChatId()).text(MessageText.SUCCESS_EDIT_HEIGHT.getMessageText()).build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return messageUtil.getEditProfileMessage(message, springWebhookBot);
    }

    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery, SpringWebhookBot springWebhookBot) {
        int messageId = callbackQuery.getMessage().getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();
        long allowId = Long.parseLong(callbackQuery.getData().split(":")[1]);
        long userId = callbackQuery.getFrom().getId();
        if (allowId == userId) {
            EditMessageText message = EditMessageText
                    .builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(MessageText.REQUEST_HEIGHT.getMessageText())
                    .build();
            telegramConfig.getUserStateMap().put(callbackQuery.getFrom().getId(), CallbackName.UPDATE_HEIGHT);
            return message;
        }
        return null;
    }
}
