package tg.fitnessbot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.constants.CommandName;


public class UnknownCommand extends AbstractCommand implements Command{
    CommandName commandName;

    @Override
    public BotApiMethod<?> handleCommand(Message message) {
        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text("Прости, я не знаю такой команды :(")
                .build();
    }

}
