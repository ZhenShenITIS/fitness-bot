package tg.fitnessbot.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tg.fitnessbot.config.TelegramConfig;

import java.util.Arrays;

@Component
public class UserUtil {

    @Autowired
    TelegramConfig telegramConfig;

    public boolean isAdmin (Long id) {
        return getAdmins().length > 0 && Arrays.stream(getAdmins()).filter(l -> l.equals(id)).toArray().length > 0;
    }

    private Long[] getAdmins() {
        Long[] admins = new Long[telegramConfig.getAdminsId().split(",").length];
        for (int i = 0; i < admins.length; i++) {
            admins[i] = Long.parseLong(telegramConfig.getAdminsId().split(",")[i]);
        }
        return admins;
    }
}
