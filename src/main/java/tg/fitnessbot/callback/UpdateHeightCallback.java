package tg.fitnessbot.callback;

import jakarta.websocket.MessageHandler;
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
public class UpdateHeightCallback extends AbstractCallback implements Callback {
    CallbackName callbackName = CallbackName.UPDATE_HEIGHT;

    @Autowired
    UserService userService;

    @Autowired
    TelegramConfig telegramConfig;

    @Override
    public BotApiMethod<?> answerMessage(Message message) {
        UserForm user = userService.getUserByID(message.getFrom().getId());
        try {
            user.setHeight(Integer.parseInt(message.getText().replaceAll(",",".")));
        } catch (NumberFormatException e) {
            return SendMessage.builder().chatId(message.getChatId()).text("Неправильно введён рост!\nТребуется целое число").build();
        }
        userService.updateUser(user);
        telegramConfig.getUserStateMap().put(user.getId(), CallbackName.NONE);
        return SendMessage.builder().chatId(message.getChatId()).text("Ваш рост успешно обновлен!").build();
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
                    .text("Введите ваш рост:")
                    .build();
            telegramConfig.getUserStateMap().put(callbackQuery.getFrom().getId(), CallbackName.UPDATE_HEIGHT);
            return editMessageText;
        }
        return null;
    }
}
