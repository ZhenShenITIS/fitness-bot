package tg.fitnessbot.constants;

import org.springframework.beans.factory.annotation.Autowired;
import tg.fitnessbot.config.TelegramConfig;

public enum CommandName {

    START("/start"), ADD_FOOD("/add_food");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
