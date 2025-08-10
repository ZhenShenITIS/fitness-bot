package tg.fitnessbot.callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.Gender;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.UserService;

@Component
public class UpdateGenderCallback implements Callback {
    CallbackName callbackName = CallbackName.UPDATE_GENDER;

    @Autowired
    UserService userService;

    @Autowired
    TelegramConfig telegramConfig;

    @Override
    public CallbackName getCallback(){
        return callbackName;
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message) {
        UserForm user = userService.getUserByID(message.getFrom().getId());
        if (message.getText().toLowerCase().equals("м")) {
            user.setGender(Gender.MALE);
        } else if (message.getText().toLowerCase().equals("ж")) {
            user.setGender(Gender.FEMALE);
        } else {
            return SendMessage.builder().chatId(message.getChatId()).text("Неправильно введён пол!\nТребуется символ м или ж").build();
        }
        userService.updateUser(user);
        telegramConfig.getUserStateMap().put(user.getId(), CallbackName.NONE);
        return SendMessage.builder().chatId(message.getChatId()).text("Ваш пол успешно обновлен!").build();
    }

    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery) {
        int messageId = callbackQuery.getMessage().getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();
        long allowId = Long.parseLong(callbackQuery.getData().split(":")[1]);
        long userId = callbackQuery.getFrom().getId();
        if (allowId == userId) {
            EditMessageText editMessageText = EditMessageText
                    .builder()
                    .messageId(messageId)
                    .chatId(chatId)
                    .text("Введите ваш пол:\n\nПодсказка: пришлите одну букву м или ж")
                    .build();
            telegramConfig.getUserStateMap().put(callbackQuery.getFrom().getId(), CallbackName.UPDATE_GENDER);
            return editMessageText;
        }
        return null;
    }
}
