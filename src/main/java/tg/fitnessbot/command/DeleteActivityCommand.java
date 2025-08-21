package tg.fitnessbot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.constants.CommandName;
@Component
public class DeleteActivityCommand implements Command {
    CommandName commandName = CommandName.DELETE_ACTIVITY;

    @Override
    public CommandName getCommand() {
        return commandName;
    }

    @Override
    public BotApiMethod<?> handleCommand(Message message) {
        return null;
    }
}
