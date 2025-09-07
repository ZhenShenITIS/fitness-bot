package tg.fitnessbot.constants;

public enum CallbackName {
    UPDATE_WEIGHT("update_weight"),
    UPDATE_HEIGHT("update_height"),
    UPDATE_BIRTHDAY("update_birthday"),
    UPDATE_GENDER("update_gender"),
    UPDATE_LIFE_ACTIVITY("update_activity"),
    UPDATE_PHOTO("update_photo"),
    EDIT_PROFILE("edit_profile"),
    AFTER_CALLBACK("after_callback"),
    NONE("none");

    private final String callbackName;

    CallbackName(String commandName) {
        this.callbackName = commandName;
    }

    public String getCallbackName() {
        return callbackName;
    }
}
