package tg.fitnessbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.config.TelegramConfig;

import java.util.Arrays;

import static tg.fitnessbot.constants.CommandName.ADD_FOOD;

@Component
public class AddFoodCommand implements Command{
    @Autowired
    TelegramConfig telegramConfig;

    @Override
    public BotApiMethod<?> handleCommand(Message message) {
        // TODO Сделать более красивую проверку админа
        if (telegramConfig.getAdmins().length > 0 && Arrays.stream(telegramConfig.getAdmins()).filter(l -> l.equals(message.getChat().getId())).toArray().length > 0) {
            // TODO Реализовать функционал добавления еды в базу
            String cmdText = message.getText().substring(ADD_FOOD.getCommandName().length());

            SendMessage messageToSend = SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text("Текст вашей команды: " + cmdText)
                    .build();
            return messageToSend;
        } else {
            SendMessage messageToSend = SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text("Вы не являетесь админом!")
                    .build();
            return messageToSend;
        }
    }
}
