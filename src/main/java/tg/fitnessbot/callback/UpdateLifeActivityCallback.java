package tg.fitnessbot.callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.UserService;
import tg.fitnessbot.utils.MessageUtil;

@Component
public class UpdateLifeActivityCallback implements Callback{
    CallbackName callbackName = CallbackName.UPDATE_LIFE_ACTIVITY;

    @Autowired
    TelegramConfig telegramConfig;

    @Autowired
    UserService userService;

    @Autowired
    MessageUtil messageUtil;

    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery) {
        return null;
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message) {
        UserForm user = userService.getUserByID(message.getFrom().getId());
        try {
            //user.setLifeActivity(Integer.parseInt(message.getText().replaceAll(",",".")));
        } catch (NumberFormatException e) {
            return SendMessage.builder().chatId(message.getChatId()).text(MessageText.WRONG_HEIGHT.getMessageText()).build();
        }
        userService.updateUser(user);
        telegramConfig.getUserStateMap().put(user.getId(), CallbackName.NONE);
        return messageUtil.getProfileMessage(message);
    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
