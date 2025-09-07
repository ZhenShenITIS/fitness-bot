package tg.fitnessbot.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.constants.CommandName;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.SignUpService;
import tg.fitnessbot.utils.MessageUtil;

import static tg.fitnessbot.constants.CommandName.START;

@Component
public class StartCommand implements Command{
    CommandName commandName = START;

    @Autowired
    SignUpService signUpService;

    @Autowired
    MessageUtil messageUtil;

    @Override
    public CommandName getCommand() {
        return commandName;
    }

    @Override
    public BotApiMethod<?> handleCommand(Message message, SpringWebhookBot springWebhookBot) {
            UserForm user = new UserForm();
            user.setFirstName(message.getFrom().getFirstName());
            user.setLastName(message.getFrom().getLastName());
            user.setUsername(message.getFrom().getUserName());
            user.setId(message.getFrom().getId());
            if (signUpService.signUp(user)) {
                SendMessage messageToSend = SendMessage
                        .builder()
                        .chatId(message.getChatId())
                        .text(MessageText.SUCCESS_REGISTRATION.getMessageText())
                        .build();

                return messageToSend;
            } else {
                return messageUtil.getProfileMessage(message, springWebhookBot);
            }
    }





}
