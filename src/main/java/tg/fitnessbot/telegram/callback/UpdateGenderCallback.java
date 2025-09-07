package tg.fitnessbot.telegram.callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.Gender;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.UserService;
import tg.fitnessbot.utils.MessageUtil;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateGenderCallback implements Callback {
    CallbackName callbackName = CallbackName.UPDATE_GENDER;

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
        if (message.getText().toLowerCase().equals("м")) {
            user.setGender(Gender.MALE);
        } else if (message.getText().toLowerCase().equals("ж")) {
            user.setGender(Gender.FEMALE);
        } else {
            return SendMessage.builder().chatId(message.getChatId()).text(MessageText.WRONG_GENDER.getMessageText()).build();
        }
        userService.updateUser(user);
        telegramConfig.getUserStateMap().put(user.getId(), CallbackName.NONE);
        try {
            springWebhookBot.execute(SendMessage
                    .builder()
                            .text(MessageText.SUCCESS_EDIT_GENDER.getMessageText())
                            .chatId(message.getChatId())
                            .replyMarkup(new ReplyKeyboardRemove(true))
                    .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return messageUtil.getEditProfileMessage(message, springWebhookBot);
    }

    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery, SpringWebhookBot springWebhookBot) {
        // TODO Сделать реплей маркап
        int messageId = callbackQuery.getMessage().getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();
        long allowId = Long.parseLong(callbackQuery.getData().split(":")[1]);
        long userId = callbackQuery.getFrom().getId();
        if (allowId == userId) {
            DeleteMessage deleteMessage = DeleteMessage
                    .builder()
                    .messageId(messageId)
                    .chatId(chatId)
                    .build();
            try {
                springWebhookBot.execute(deleteMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            List<KeyboardButton> listOfButtons = new ArrayList<>();
            listOfButtons.add(KeyboardButton.builder().text("М").build());
            listOfButtons.add(KeyboardButton.builder().text("Ж").build());
            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(MessageText.REQUEST_GENDER.getMessageText())
                    .replyMarkup(ReplyKeyboardMarkup
                            .builder()
                            .keyboardRow(new KeyboardRow(listOfButtons))
                            .build())
                    .build();
            telegramConfig.getUserStateMap().put(callbackQuery.getFrom().getId(), CallbackName.UPDATE_GENDER);

            return message;
        }
        return null;
    }
}
