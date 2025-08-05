package tg.fitnessbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.SignUpService;
import tg.fitnessbot.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartCommand implements Command{

    @Autowired
    SignUpService signUpService;

    @Autowired
    UserService userService;

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
                user = userService.getUserByID(message.getFrom().getId());
                String height = user.getHeight() == null ? "не указан" : user.getHeight().toString();
                String weight = user.getWeight() == null ? "не указан" : user.getWeight().toString();
                String birthday = user.getBirthday() == null ? "не указан" : user.getBirthday().toString();
                String gender = user.getGender() == null ? "не указан" : user.getGender().toString();
                String textToSend = "Привет! Вот твои данные: \n" +
                        "Рост: " + height + "\n" +
                        "Вес: " + weight + "\n" +
                        "Дата рождения: " + birthday + "\n" +
                        "Пол: " + gender + "\n\n" +
                        "Хочешь их изменить?";
                SendMessage messageToSend = SendMessage
                        .builder()
                        .chatId(message.getChatId())
                        .text(textToSend)
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboardRow(List.of(
                                        InlineKeyboardButton
                                                .builder()
                                                .text("Рост")
                                                .callbackData(CallbackName.UPDATE_HEIGHT.getCallbackName())
                                                .build(),
                                        InlineKeyboardButton
                                                .builder()
                                                .text("Вес")
                                                .callbackData(CallbackName.UPDATE_WEIGHT.getCallbackName())
                                                .build()))
                                .keyboardRow(List.of(
                                        InlineKeyboardButton
                                                .builder()
                                                .text("ДР")
                                                .callbackData(CallbackName.UPDATE_BIRTHDAY.getCallbackName())
                                                .build(),
                                        InlineKeyboardButton
                                                .builder()
                                                .text("Пол")
                                                .callbackData(CallbackName.UPDATE_GENDER.getCallbackName())
                                                .build()))
                                .build())
                        .build();
                return messageToSend;
            }
        }
        return null;
    }




}
