package tg.fitnessbot.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramConfig {
    @Value("${TELEGRAM_BOT_WEBHOOK_URL}")
    String webhookPath;
    @Value("${TELEGRAM_BOT_USERNAME}")
    String botName;
    @Value("${TELEGRAM_BOT_TOKEN}")
    String botToken;
    @Value("${TELEGRAM_BOT_ADMINS_ID}")
    String adminsId;

    public Long[] getAdmins() {
        Long[] admins = new Long[adminsId.split(",").length];
        for (int i = 0; i < admins.length; i++) {
            admins[i] = Long.parseLong(adminsId.split(",")[i]);
        }
        return admins;
    }

}
