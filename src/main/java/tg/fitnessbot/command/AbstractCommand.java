package tg.fitnessbot.command;

import tg.fitnessbot.constants.CommandName;

public abstract class AbstractCommand implements Command{
    CommandName commandName;

    @Override
    public CommandName getCommand() {
        return commandName;
    }
}
