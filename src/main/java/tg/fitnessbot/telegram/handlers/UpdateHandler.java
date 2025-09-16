package tg.fitnessbot.telegram.handlers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.models.ProfilePhoto;
import tg.fitnessbot.services.AI.AudioTranscriptionService;
import tg.fitnessbot.services.AI.LLMService;
import tg.fitnessbot.services.FileService;
import tg.fitnessbot.services.ProfilePhotoService;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;

import java.util.Arrays;

@Getter
@Setter
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateHandler extends SpringWebhookBot {

    String botPath;
    String botUsername;
    String botToken;

    @Autowired
    TelegramConfig telegramConfig;

    @Autowired
    LLMService llmService;

    @Autowired
    MessageHandler messageHandler;

    @Autowired
    CallbackQueryHandler callbackQueryHandler;

    @Autowired
    ProfilePhotoService profilePhotoService;

    public UpdateHandler(SetWebhook setWebhook) {
        super(setWebhook);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            //TODO Добавить обработку исключения при неправильных сообщениях в личных чатах
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery, this);
        } else {
            // TODO Убрать это
            Message message = update.getMessage();
            if (message != null) {
                if (message.hasText() && message.getText().equals("картинка")) {
                    String fileId = profilePhotoService.getPhotoFileId(message.getFrom().getId());

                    if (fileId != null) {
                        SendPhoto msg = SendPhoto
                                .builder()
                                .chatId(message.getChatId())
                                .caption("Вот твое фото профиля")
                                .photo(new InputFile(fileId))
                                .build();
                        try {
                            this.execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        SendMessage msg = SendMessage
                                .builder()
                                .chatId(message.getChatId())
                                .text("Фото профиля не установлено")
                                .build();
                        try {
                            this.execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
                return messageHandler.answerMessage(update.getMessage(), this);


            }
        }
        return null;
    }
}
