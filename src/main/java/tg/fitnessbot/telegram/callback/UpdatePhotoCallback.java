package tg.fitnessbot.telegram.callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.models.ProfilePhoto;
import tg.fitnessbot.services.ProfilePhotoService;
import tg.fitnessbot.services.UserService;
import tg.fitnessbot.utils.MessageUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class UpdatePhotoCallback implements Callback {
    CallbackName callbackName = CallbackName.UPDATE_PHOTO;

    @Autowired
    TelegramConfig telegramConfig;

    @Autowired
    UserService userService;

    @Autowired
    MessageUtil messageUtil;

    @Autowired
    ProfilePhotoService profilePhotoService;

    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery) {
        int messageId = callbackQuery.getMessage().getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();
        long allowId = Long.parseLong(callbackQuery.getData().split(":")[1]);
        long userId = callbackQuery.getFrom().getId();
        if (allowId == userId) {
            EditMessageText editMessageText = EditMessageText
                    .builder()
                    .messageId(messageId)
                    .chatId(chatId)
                    .text(MessageText.REQUEST_PHOTO.getMessageText())
                    .build();
            telegramConfig.getUserStateMap().put(callbackQuery.getFrom().getId(), CallbackName.UPDATE_PHOTO);
            return editMessageText;
        }
        return null;
    }


    // TODO Доделать
    @Override
    public BotApiMethod<?> answerMessage(Message message) {
            List<PhotoSize> photos = message.getPhoto();
            String fileId = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).map(PhotoSize::getFileId).orElse("");
            System.out.println("Отладочный вывод 1: userID = " + message.getFrom().getId() + ", fileID = " + fileId);
            profilePhotoService.setPhoto(message.getFrom().getId(),fileId);
            System.out.println("Отладочный вывод 2");
            telegramConfig.getUserStateMap().put(message.getFrom().getId(), CallbackName.NONE);
            return messageUtil.getProfileMessage(message);

//        System.out.println("Отладочный вывод перед отправкой сообщения с неверным фото");
//        return SendMessage.builder().chatId(message.getChatId()).text(MessageText.WRONG_PHOTO.getMessageText()).build();

    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
