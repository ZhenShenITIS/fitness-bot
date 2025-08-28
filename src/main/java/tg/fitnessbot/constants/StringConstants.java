package tg.fitnessbot.constants;

public enum StringConstants {
    ACTIVITY("тренировка"),
    FOOD("еда")
    ;

    private String value;

    StringConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
