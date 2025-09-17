package tg.fitnessbot.telegram.command.impl;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.constants.CommandName;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.telegram.command.Command;


public class UnknownCommand implements Command {
    CommandName commandName;

    @Override
    public BotApiMethod<?> handleCommand(Message message, SpringWebhookBot springWebhookBot) {
        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(MessageText.UNKNOWN_COMMAND.getMessageText())
                .build();
    }
    @Override
    public CommandName getCommand() {
        return commandName;
    }

}
