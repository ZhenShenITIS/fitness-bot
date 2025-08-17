package tg.fitnessbot.constants;

public enum Gender {
    MALE("мужской"), FEMALE("женский");

    private final String genderName;

    Gender(String genderName) {
        this.genderName = genderName;
    }

    public String getGenderName() {
        return genderName;
    }
}
