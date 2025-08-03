package tg.fitnessbot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.SignUpService;

import java.util.Arrays;

@Component
public class MessageHandlerImpl implements MessageHandler {

    @Autowired
    SignUpService signUpService;

    @Autowired
    TelegramConfig telegramConfig;

    @Override
    public BotApiMethod<?> answerMessage(Message message) {
        if (message.hasText()) {
                if (message.getText().equals("/start")) {
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

                } else if (message.getText().equals("/add_food")) {
                    // TODO Сделать более красивую проверку админа
                    if (telegramConfig.getAdmins().length > 0 && Arrays.stream(telegramConfig.getAdmins()).filter(l -> l.equals(message.getChat().getId())).toArray().length > 0) {
                        // TODO Реализовать функционал добавления еды в базу
                        SendMessage messageToSend = SendMessage
                                .builder()
                                .chatId(message.getChatId())
                                .text("Данный функционал пока не реализован")
                                .build();
                        return messageToSend;
                    } else {
                        SendMessage messageToSend = SendMessage
                                .builder()
                                .chatId(message.getChatId())
                                .text("Вы не являетесь админом!")
                                .build();
                        return messageToSend;
                    }
                }
            }
        return null;
    }
}
