package tg.fitnessbot.command;

import tg.fitnessbot.constants.CommandName;

public abstract class AbstractCommand implements Command{
    CommandName commandName;

    @Override
    public String getCommandName() {
        return commandName.getCommandName();
    }
}
