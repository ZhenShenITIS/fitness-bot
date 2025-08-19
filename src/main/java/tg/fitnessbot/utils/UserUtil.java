package tg.fitnessbot.utils;

import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

public class UserUtil {

    // TODO Убрать тут админов и сделать подтягивание из тг-конфига, просто тестим статический контекст
    @Value("${TELEGRAM_BOT_ADMINS_ID}")
    static String adminsId;

    public static boolean isAdmin (Long id) {
        return getAdmins().length > 0 && Arrays.stream(getAdmins()).filter(l -> l.equals(id)).toArray().length > 0;
    }

    public static Long[] getAdmins() {
        Long[] admins = new Long[adminsId.split(",").length];
        for (int i = 0; i < admins.length; i++) {
            admins[i] = Long.parseLong(adminsId.split(",")[i]);
        }
        return admins;
    }
}
