package tg.fitnessbot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.command.AddFoodCommand;
import tg.fitnessbot.command.CommandContainer;
import tg.fitnessbot.command.StartCommand;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.SignUpService;

import java.util.Arrays;

@Component
public class MessageHandlerImpl implements MessageHandler {

    @Autowired
    CommandContainer commandContainer;

    @Override
    public BotApiMethod<?> answerMessage(Message message) {
        if (message.hasText()) {
            if (message.getText().startsWith("/")) {
                String commandIdentifier = message.getText().split(" ")[0].split("\n")[0].toLowerCase();
                return commandContainer.retrieveCommand(commandIdentifier).handleCommand(message);
            } else {
                // TODO Реализовать логику работы сообщения, не содержащего команды
                return SendMessage.builder().chatId(message.getChatId()).text("Вы не ввели никакой команды").build();
            }
        }
        return null;
    }
}
