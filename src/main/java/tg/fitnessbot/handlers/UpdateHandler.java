package tg.fitnessbot.handlers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.SignUpService;

import java.util.Arrays;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateHandler extends SpringWebhookBot {

    String botPath;
    String botUsername;
    String botToken;
    @Autowired
    SignUpService signUpService;

    @Autowired
    TelegramConfig telegramConfig;

    public UpdateHandler(SetWebhook setWebhook) {
        super(setWebhook);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                if (update.getMessage().getText().equals("/start")) {
                    if (update.getMessage().getChat().isUserChat()) {
                        UserForm user = new UserForm();
                        user.setFirstName(update.getMessage().getFrom().getFirstName());
                        user.setLastName(update.getMessage().getFrom().getLastName());
                        user.setUsername(update.getMessage().getFrom().getUserName());
                        user.setId(update.getMessage().getFrom().getId());
                        if (signUpService.signUp(user)) {
                            SendMessage message = SendMessage
                                    .builder()
                                    .chatId(update.getMessage().getChatId())
                                    .text("Вы успешно зарегестрировались!")
                                    .build();
                            return message;
                        } else {
                            SendMessage message = SendMessage
                                    .builder()
                                    .chatId(update.getMessage().getChatId())
                                    .text("Вы уже зарегистрированны в боте!")
                                    .build();
                            return message;
                        }
                    }

                } else if (update.getMessage().getText().equals("/add_food")) {
                    if (telegramConfig.getAdmins().length > 0 && Arrays.stream(telegramConfig.getAdmins()).filter(l -> l.equals(update.getMessage().getChat().getId())).toArray().length > 0) {
                        //добавляем еду
                        SendMessage message = SendMessage
                                .builder()
                                .chatId(update.getMessage().getChatId())
                                .text("Данный функционал пока не реализован")
                                .build();
                        return message;
                    } else {
                        SendMessage message = SendMessage
                                .builder()
                                .chatId(update.getMessage().getChatId())
                                .text("Вы не являетесь админом!")
                                .build();
                        return message;
                    }
                }
            }
        }
        return null;
    }
}
