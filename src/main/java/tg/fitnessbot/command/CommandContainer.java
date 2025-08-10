package tg.fitnessbot.command;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static tg.fitnessbot.constants.CommandName.ADD_FOOD;
import static tg.fitnessbot.constants.CommandName.START;

@Component
public class CommandContainer {
    private final ImmutableMap<String, Command> commands;
    private final Command unknownCommand;

    public CommandContainer(Command[] commandArray) {
        HashMap<String, Command> map = new HashMap<String, Command>();
        for (Command command : commandArray) {
            map.put(command.getCommand().getCommandName(), command);
        }
        commands = ImmutableMap.copyOf(map);
        unknownCommand = new UnknownCommand();
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commands.getOrDefault(commandIdentifier, unknownCommand);
    }
}
