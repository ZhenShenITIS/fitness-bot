package tg.fitnessbot.telegram.handlers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.services.AI.AudioTranscriptionService;
import tg.fitnessbot.services.FileService;
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
    MessageHandler messageHandler;

    @Autowired
    CallbackQueryHandler callbackQueryHandler;

    @Autowired
    AudioTranscriptionService audioTranscriptionService;

    @Autowired
    FileService fileService;

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

    private BotApiMethod<?> handleUpdate (Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        } else {
            Message message = update.getMessage();
            if (message != null) {
                if (message.hasVoice()) {
                    SendMessage msg;
                    String textToSend = "";
                    java.io.File file = fileService.getVoiceFile(message);
                    MultimediaObject object = new MultimediaObject(file);
                    long duration;
                    try {
                        duration = object.getInfo().getDuration();
                    } catch (EncoderException e) {
                        throw new RuntimeException(e);
                    }
                    if (duration > 90000) {
                        textToSend = "Слишком длинное аудио!";
                        file.delete();
                    } else {
                        textToSend = audioTranscriptionService.transcribeAudio(file);
                    }


                    if (textToSend.length() > 4090) {
                        textToSend = textToSend.substring(0, 4000) + "...";
                    }
                    msg = SendMessage.builder().text("Транскрипция: "+textToSend).chatId(message.getChatId()).build();
                    return msg;
                } else  {
                    return messageHandler.answerMessage(update.getMessage());
                }

            }
        }
        return null;
    }
}
