package tg.fitnessbot.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.constants.CommandName;

public class UpdateActivityCommand implements Command {
    CommandName commandName = CommandName.UPDATE_ACTIVITY;

    @Override
    public CommandName getCommand() {
        return commandName;
    }

    @Override
    public BotApiMethod<?> handleCommand(Message message) {
        return null;
    }
}
