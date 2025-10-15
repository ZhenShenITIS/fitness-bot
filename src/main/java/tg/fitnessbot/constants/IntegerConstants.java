package tg.fitnessbot.constants;



public enum IntegerConstants {
    NUMBER_OF_SUCCESS_LINES(5),
    BASE_SUBSCRIPTION_COST(1),
    MAX_VOICE_DURATION(90_000),
    COUNT_OF_TRIAL_ATTEMPTS(5)
    ;

    private final Integer value;

    IntegerConstants(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
