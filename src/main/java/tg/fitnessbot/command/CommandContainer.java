package tg.fitnessbot.command;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static tg.fitnessbot.constants.CommandName.ADD_FOOD;
import static tg.fitnessbot.constants.CommandName.START;

@Component
public class CommandContainer {
    private final ImmutableMap<String, Command> commands;
    private final Command unknownCommand;

    public CommandContainer(StartCommand startCommand, AddFoodCommand addFoodCommand) {
        commands = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), startCommand)
                .put(ADD_FOOD.getCommandName(), addFoodCommand)
                .build();
        unknownCommand = new UnknownCommand();
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commands.getOrDefault(commandIdentifier, unknownCommand);
    }
}
