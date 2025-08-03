package tg.fitnessbot.command;

import com.google.common.collect.ImmutableMap;
import org.glassfish.jersey.internal.util.collection.ImmutableMultivaluedMap;
import org.springframework.stereotype.Component;

import static tg.fitnessbot.constants.CommandName.ADD_FOOD;
import static tg.fitnessbot.constants.CommandName.START;

@Component
public class CommandContainer {
    private final Command unknownCommand;
    private final ImmutableMap<String, Command> commands;

    public CommandContainer() {
        commands = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), new StartCommand())
                .put(ADD_FOOD.getCommandName(), new AddFoodCommand())
                .build();
        unknownCommand = new UnknownCommand();
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commands.getOrDefault(commandIdentifier, unknownCommand);
    }
}
