package tg.fitnessbot.constants;



public enum IntegerConstants {
    NUMBER_OF_SUCCESS_LINES(5),
    MAX_VOICE_DURATION(90_000)
    ;

    private final Integer value;

    IntegerConstants(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
