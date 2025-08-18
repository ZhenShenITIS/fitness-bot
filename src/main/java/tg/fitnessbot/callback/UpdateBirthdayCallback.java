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
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.UserService;
import tg.fitnessbot.utils.MessageUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
public class UpdateBirthdayCallback implements Callback {
    CallbackName callbackName = CallbackName.UPDATE_BIRTHDAY;
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
    public BotApiMethod<?> answerMessage(Message message) {
        UserForm user = userService.getUserByID(message.getFrom().getId());
        try {
            user.setBirthday(LocalDate.parse(message.getText().replaceAll(",",".")));
        } catch (DateTimeParseException e) {
            return SendMessage.builder().chatId(message.getChatId()).text("Неправильно введена дата рождения!\nТребуется строка в формате 2000-12-31").build();
        }
        userService.updateUser(user);
        telegramConfig.getUserStateMap().put(user.getId(), CallbackName.NONE);
        return messageUtil.getProfileMessage(message);
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
                    .text("Введите вашу дату рождения:\n\nПодсказка: если вы волнуетесь о сохранности ваших персональных данных, можете ввести примерную дату +- 3 года\n\nЭти данные нужны для правильного подсчета каллорий на тренировках")
                    .build();
            telegramConfig.getUserStateMap().put(callbackQuery.getFrom().getId(), CallbackName.UPDATE_BIRTHDAY);
            return editMessageText;
        }
        return null;
    }
}
