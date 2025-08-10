package tg.fitnessbot.command;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static tg.fitnessbot.constants.CommandName.ADD_FOOD;
import static tg.fitnessbot.constants.CommandName.START;

@Component
public class CommandContainer {
    private final HashMap<String, Command> commands;
    private final Command unknownCommand;

    public CommandContainer(Command[] commandArray) {
        commands = new HashMap<String, Command>();
        for (Command command : commandArray) {
            commands.put(command.getCommand().getCommandName(), command);
        }
        unknownCommand = new UnknownCommand();
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commands.getOrDefault(commandIdentifier, unknownCommand);
    }
}
