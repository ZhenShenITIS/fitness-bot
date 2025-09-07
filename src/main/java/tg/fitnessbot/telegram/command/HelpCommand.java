package tg.fitnessbot.telegram.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.constants.CommandName;
import tg.fitnessbot.constants.MessageText;

@Component
public class HelpCommand implements Command{
    CommandName commandName = CommandName.HELP;

    @Override
    public CommandName getCommand() {
        return commandName;
    }

    @Override
    public BotApiMethod<?> handleCommand(Message message, SpringWebhookBot springWebhookBot) {
        return SendMessage.builder().chatId(message.getChatId()).text(MessageText.HELP.getMessageText()).build();
    }
}
