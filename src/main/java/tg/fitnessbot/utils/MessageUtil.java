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
import tg.fitnessbot.constants.Gender;
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
        String lifeActivity = user.getLifeActivity() == null ? "не указана" : user.getLifeActivity().getActivityName();
        Double tdee = 0.0;
        if (height != null && weight != null && birthday != null && gender != null && lifeActivity != null) {
            Double bmr;
            if (user.getGender().equals(Gender.MALE)) {
                // BMR = 10 × вес (кг) + 6.25 × рост (см) – 5 × возраст (г) + 5
                bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * DateUtil.getAge(user.getBirthday()) + 5;
            } else {
                // BMR = 10 × вес (кг) + 6.25 × рост (см) – 5 × возраст (г) – 161
                bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * DateUtil.getAge(user.getBirthday()) - 161;
            }
            tdee = bmr * user.getLifeActivity().getRatio();
        }
        String tdeeStr = tdee.equals(0.0) ? "невозможно посчитать, заполнить профиль полностью" : tdee.toString();
        String textToSend = String.format(MessageText.PROFILE.getMessageText(), height, weight, birthday, gender, lifeActivity, tdeeStr);
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
                                        .text(MessageText.INLINE_BUTTON_LIFE_ACTIVITY.getMessageText())
                                        .callbackData(CallbackName.UPDATE_LIFE_ACTIVITY.getCallbackName()+":"+user.getId())
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
