package tg.fitnessbot.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.constants.CommandName;

public interface Command {
    String getCommandName ();
    BotApiMethod<?> handleCommand(Message message);
}
