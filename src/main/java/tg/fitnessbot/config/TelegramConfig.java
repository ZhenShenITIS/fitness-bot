package tg.fitnessbot.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tg.fitnessbot.constants.CallbackName;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


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

    // TODO возможно не стоит эту мапу тут хранить. Подумать над этим
    final Map<Long, CallbackName> userStateMap = new ConcurrentHashMap<>();

//    public Long[] getAdmins() {
//        Long[] admins = new Long[adminsId.split(",").length];
//        for (int i = 0; i < admins.length; i++) {
//            admins[i] = Long.parseLong(adminsId.split(",")[i]);
//        }
//        return admins;
//    }

}
