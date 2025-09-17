package tg.fitnessbot.telegram.handlers.bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.services.AI.LLMService;
import tg.fitnessbot.services.ProfilePhotoService;
import tg.fitnessbot.telegram.handlers.CallbackQueryHandler;
import tg.fitnessbot.telegram.handlers.MessageHandler;

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
            try {
                this.execute(callbackQueryHandler.processCallbackQuery(callbackQuery, this));
            } catch (TelegramApiException e) {
                System.out.println("TelegramApiException: " + e.getMessage());
            }
            return null;
        } else {
            Message message = update.getMessage();
            if (message != null) {
                try {
                    this.execute(messageHandler.answerMessage(update.getMessage(), this));
                } catch (TelegramApiException e) {
                    System.out.println("TelegramApiException: " + e.getMessage());
                }
                return null;
            }
        }
        return null;
    }
}
