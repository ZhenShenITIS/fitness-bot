package tg.fitnessbot.telegram.callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.MessageText;

import java.util.ArrayList;
import java.util.List;
@Component
public class EditProfileCallback implements Callback {
    CallbackName callbackName = CallbackName.EDIT_PROFILE;
    @Autowired
    TelegramConfig telegramConfig;

    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery, SpringWebhookBot springWebhookBot) {
        int messageId = callbackQuery.getMessage().getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();
        long allowId = Long.parseLong(callbackQuery.getData().split(":")[1]);
        User user = callbackQuery.getFrom();
        long userId = callbackQuery.getFrom().getId();
        if (allowId == userId) {
            DeleteMessage deleteMessage = DeleteMessage.builder().chatId(chatId).messageId(messageId).build();

            telegramConfig.getUserStateMap().put(callbackQuery.getFrom().getId(), CallbackName.EDIT_PROFILE);

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
                            .build())
                    .build();
        }
        return null;
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message, SpringWebhookBot springWebhookBot) {
        return null;
    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
