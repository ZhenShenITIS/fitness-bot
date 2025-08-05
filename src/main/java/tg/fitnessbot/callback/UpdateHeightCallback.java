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
public class UpdateHeightCallback implements Callback {

    @Autowired
    UserService userService;

    @Autowired
    TelegramConfig telegramConfig;

    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery, Long userId) {
        return null;
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message) {
        return null;
    }
}
