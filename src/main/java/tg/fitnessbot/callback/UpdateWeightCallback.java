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

@Component
public class UpdateWeightCallback implements Callback {
    @Autowired
    TelegramConfig telegramConfig;

    @Autowired
    UserService userService;

    @Override
    public BotApiMethod<?> answerMessage(Message message) {
        UserForm user = userService.getUserByID(message.getFrom().getId());
        try {
            user.setWeight(Double.parseDouble(message.getText().replaceAll(",",".")));
        } catch (NumberFormatException e) {
            return SendMessage.builder().chatId(message.getChatId()).text("Неправильно введён вес!").build();
        }
        userService.updateUser(user);
        telegramConfig.getUserStateMap().put(user.getId(), CallbackName.NONE);
        return SendMessage.builder().chatId(message.getChatId()).text("Ваш вес успешно обновлен!").build();
    }

    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery) {
        int messageId = callbackQuery.getMessage().getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();
            EditMessageText editMessageText = EditMessageText
                    .builder()
                    .messageId(messageId)
                    .chatId(chatId)
                    .text("Введите ваш вес:")
                    .build();
            telegramConfig.getUserStateMap().put(callbackQuery.getFrom().getId(), CallbackName.UPDATE_WEIGHT);
            return editMessageText;
    }
}
