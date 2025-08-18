package tg.fitnessbot.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.command.StartCommand;
import tg.fitnessbot.constants.CommandName;

@Component
@Data
public class MessageUtil {

    @Autowired
    static StartCommand startCommand;

    public static BotApiMethod<?> getStartMessage(Message message) {
        message.setText(CommandName.START.getCommandName());
        return startCommand.handleCommand(message);
    }
}
