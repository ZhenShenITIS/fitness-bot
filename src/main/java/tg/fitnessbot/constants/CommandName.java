package tg.fitnessbot.constants;

public enum CommandName {

    START("/start"),
    CALCULATE_FOOD("/food"),
    DELETE_FOOD("/delete_food"),
    UPDATE_FOOD("/update_food"),
    ADD_FOOD("/add_food"),
    CALCULATE_ACTIVITY("/activity"),
    ADD_ACTIVITY("/add_activity"),
    UPDATE_ACTIVITY("/update_activity"),
    DELETE_ACTIVITY("/delete_activity"),
    HELP("/help");


    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
