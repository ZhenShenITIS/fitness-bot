package tg.fitnessbot.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageAutoDeleteTimerChanged;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tg.fitnessbot.command.StartCommand;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.CommandName;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.UserService;

import java.util.List;

@Component
@Data
public class MessageUtil {

    @Autowired
    UserService userService;

    public BotApiMethod<?> getProfileMessage(Message message) {
        UserForm user = userService.getUserByID(message.getFrom().getId());
        String height = user.getHeight() == null ? "не указан" : user.getHeight().toString();
        String weight = user.getWeight() == null ? "не указан" : user.getWeight().toString();
        String birthday = user.getBirthday() == null ? "не указан" : String.valueOf(DateUtil.getAge(user.getBirthday()));
        String gender = user.getGender() == null ? "не указан" : user.getGender().getGenderName();
        String textToSend = String.format(MessageText.PROFILE.getMessageText(), height, weight, birthday, gender);
        SendMessage messageToSend = SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(textToSend)
                .replyMarkup(InlineKeyboardMarkup
                        .builder()
                        .keyboardRow(List.of(
                                InlineKeyboardButton
                                        .builder()
                                        .text(MessageText.INLINE_BUTTON_HEIGHT.getMessageText())
                                        .callbackData(CallbackName.UPDATE_HEIGHT.getCallbackName()+":"+user.getId())
                                        .build(),
                                InlineKeyboardButton
                                        .builder()
                                        .text(MessageText.INLINE_BUTTON_WEIGHT.getMessageText())
                                        .callbackData(CallbackName.UPDATE_WEIGHT.getCallbackName()+":"+user.getId())
                                        .build()))
                        .keyboardRow(List.of(
                                InlineKeyboardButton
                                        .builder()
                                        .text(MessageText.INLINE_BUTTON_BIRTHDAY.getMessageText())
                                        .callbackData(CallbackName.UPDATE_BIRTHDAY.getCallbackName()+":"+user.getId())
                                        .build(),
                                InlineKeyboardButton
                                        .builder()
                                        .text(MessageText.INLINE_BUTTON_GENDER.getMessageText())
                                        .callbackData(CallbackName.UPDATE_GENDER.getCallbackName()+":"+user.getId())
                                        .build()))
                        .build())
                .build();
        return messageToSend;
    }
}
