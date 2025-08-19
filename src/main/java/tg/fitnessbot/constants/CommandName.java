package tg.fitnessbot.constants;

public enum CommandName {

    START("/start"),
    DELETE_FOOD("/delete_food"),
    UPDATE_FOOD("/update_food"),
    ADD_FOOD("/add_food");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
