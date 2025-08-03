package tg.fitnessbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.SignUpService;

@Component
public class StartCommand implements Command{

    @Autowired
    SignUpService signUpService;

    @Override
    public BotApiMethod<?> handleCommand(Message message) {
        if (message.getChat().isUserChat()) {
            UserForm user = new UserForm();
            user.setFirstName(message.getFrom().getFirstName());
            user.setLastName(message.getFrom().getLastName());
            user.setUsername(message.getFrom().getUserName());
            user.setId(message.getFrom().getId());
            if (signUpService.signUp(user)) {
                SendMessage messageToSend = SendMessage
                        .builder()
                        .chatId(message.getChatId())
                        .text("Вы успешно зарегестрировались!")
                        .build();
                return messageToSend;
            } else {
                SendMessage messageToSend = SendMessage
                        .builder()
                        .chatId(message.getChatId())
                        .text("Вы уже зарегистрированны в боте!")
                        .build();
                return messageToSend;
            }
        }
        return null;
    }


}
