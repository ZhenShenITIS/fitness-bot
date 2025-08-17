package tg.fitnessbot.callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.command.CommandContainer;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.CommandName;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.UserService;

@Component
public class UpdateWeightCallback implements Callback {
    CallbackName callbackName = CallbackName.UPDATE_WEIGHT;

    @Autowired
    TelegramConfig telegramConfig;

    @Autowired
    SpringWebhookBot springWebhookBot;

    @Autowired
    CommandContainer commandContainer;

    @Autowired
    UserService userService;

    @Override
    public CallbackName getCallback(){
        return callbackName;
    }

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
        SendMessage msg1 = SendMessage.builder().chatId(message.getChatId()).text("Ваш вес успешно обновлен!").build();
        SendMessage msg2 = (SendMessage) commandContainer.retrieveCommand(CommandName.START.getCommandName()).handleCommand(message);
        try {
            springWebhookBot.execute(msg1);
            springWebhookBot.execute(msg2);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return null;
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
                    .text("Введите ваш вес:")
                    .build();
            telegramConfig.getUserStateMap().put(callbackQuery.getFrom().getId(), CallbackName.UPDATE_WEIGHT);
            return editMessageText;
        }
        return null;
    }
}
