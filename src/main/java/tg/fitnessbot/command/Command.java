package tg.fitnessbot.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Command {
    BotApiMethod<?> handleCommand(Message message);
}
