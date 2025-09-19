package tg.fitnessbot.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.Gender;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.constants.StringConstants;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.ProfilePhotoService;
import tg.fitnessbot.services.UserService;

import java.util.List;

@Component
@Data
public class MessageUtil {

    @Autowired
    UserService userService;

    @Autowired
    ProfilePhotoService profilePhotoService;

    @Autowired
    TelegramConfig telegramConfig;

    public BotApiMethod<?> getProfileMessage(Message message, SpringWebhookBot springWebhookBot) {
        return getProfileMessage(message.getChatId(), message.getFrom().getId(), springWebhookBot);
    }

    public BotApiMethod<?> getProfileMessage(CallbackQuery callbackQuery, SpringWebhookBot springWebhookBot) {
        return getProfileMessage(callbackQuery.getMessage().getChatId(), callbackQuery.getFrom().getId(), springWebhookBot);
    }

    public BotApiMethod<?> getEditProfileMessage(Message message, SpringWebhookBot springWebhookBot) {
        long chatId = message.getChatId();
        User user = message.getFrom();
        return getEditProfileMessage(chatId, user);
    }

    public BotApiMethod<?> getEditProfileMessage(CallbackQuery callbackQuery, SpringWebhookBot springWebhookBot) {
        long chatId = callbackQuery.getMessage().getChatId();
        User user = callbackQuery.getFrom();
        return getEditProfileMessage(chatId, user);
    }

    private SendMessage getEditProfileMessage(long chatId, User user) {
        return SendMessage
                .builder()
                .chatId(chatId)
                .text(MessageText.REQUEST_FIELD.getMessageText())
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
                        .keyboardRow(List.of(
                                InlineKeyboardButton
                                        .builder()
                                        .text(MessageText.INLINE_BUTTON_LIFE_ACTIVITY.getMessageText())
                                        .callbackData(CallbackName.UPDATE_LIFE_ACTIVITY.getCallbackName()+":"+user.getId())
                                        .build(),
                                InlineKeyboardButton
                                        .builder()
                                        .text(MessageText.INLINE_BUTTON_PHOTO.getMessageText())
                                        .callbackData(CallbackName.UPDATE_PHOTO.getCallbackName()+":"+user.getId())
                                        .build()))
                        .keyboardRow(List.of(
                                InlineKeyboardButton
                                        .builder()
                                        .text(MessageText.INLINE_BUTTON_BACK_TO_PROFILE.getMessageText())
                                        .callbackData(CallbackName.BACK_TO_PROFILE.getCallbackName()+":"+user.getId())
                                        .build()))
                        .build())
                .build();
    }

    private BotApiMethod<?> getProfileMessage (long chatId, long userId, SpringWebhookBot springWebhookBot) {
        UserForm user = userService.getUserByID(userId);
        Double tdee = 0.0;
        if (user == null) {
            return SendMessage.builder().chatId(chatId).text(MessageText.NULL_POINTER.getMessageText()).build();
        }
        if (user.getHeight() != null && user.getWeight() != null && user.getBirthday() != null && user.getGender() != null && user.getLifeActivity() != null) {
            tdee = userService.calculateTdee(user);
        }
        String height = user.getHeight() == null ? MessageText.NOT_FILLED.getMessageText() : user.getHeight().toString();
        String weight = user.getWeight() == null ? MessageText.NOT_FILLED.getMessageText() : user.getWeight().toString();
        String birthday = user.getBirthday() == null ? MessageText.NOT_FILLED.getMessageText() : String.valueOf(DateUtil.getAge(user.getBirthday()));
        String gender = user.getGender() == null ? MessageText.NOT_FILLED.getMessageText() : user.getGender().getGenderName();
        String lifeActivity = user.getLifeActivity() == null ? MessageText.NOT_FILLED.getMessageText() : user.getLifeActivity().getActivityName();
        String tdeeStr = tdee.equals(0.0) ? MessageText.REQUIRE_FILLED_PROFILE.getMessageText() : tdee.toString();
        String textToSend = String.format(MessageText.PROFILE.getMessageText(), height, weight, birthday, gender, lifeActivity, tdeeStr);

        String fileId = profilePhotoService.getPhotoFileId(userId) == null ? StringConstants.BASE_PROFILE_PHOTO_FILE_ID.getValue() : profilePhotoService.getPhotoFileId(userId);
        SendPhoto msg = SendPhoto
                .builder()
                .chatId(chatId)
                .caption(textToSend)
                .photo(new InputFile(fileId))
                .replyMarkup(InlineKeyboardMarkup
                        .builder()
                        .keyboardRow(List.of(
                                InlineKeyboardButton
                                        .builder()
                                        .text(MessageText.EDIT_PROFILE.getMessageText())
                                        .callbackData(CallbackName.EDIT_PROFILE.getCallbackName()+":"+user.getId())
                                        .build()))
                        .build())
                .replyMarkup(new ReplyKeyboardRemove(true))
                .build();
        try {
            springWebhookBot.execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        telegramConfig.getUserStateMap().put(user.getId(), CallbackName.NONE);
        return null;
    }
}
