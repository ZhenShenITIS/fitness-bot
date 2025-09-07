package tg.fitnessbot.telegram.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.constants.CommandName;

public interface Command {
    CommandName getCommand ();
    BotApiMethod<?> handleCommand(Message message, SpringWebhookBot springWebhookBot);
}
