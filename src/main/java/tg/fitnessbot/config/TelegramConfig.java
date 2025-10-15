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

    String serviceFileInfoUri = "https://api.telegram.org/bot%s/getFile?file_id=%s";
    String serviceFileStorageUri = "https://api.telegram.org/file/bot%s/%s";

    // TODO возможно не стоит эту мапу тут хранить. Подумать над этим
    //  Решение: Использовать Redis или базу данных для хранения состояний:
    final Map<Long, CallbackName> userStateMap = new ConcurrentHashMap<>();


}
