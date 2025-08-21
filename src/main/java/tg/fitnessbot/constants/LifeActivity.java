package tg.fitnessbot.constants;

public enum LifeActivity {
    MIN("минимальная активность", 1.2),
    LIGHT("лёгкая активность", 1.375),
    MID("средняя активность", 1.55),
    HIGH("высокая активность", 1.725),
    VERY_HIGH("очень высокая активность", 1.9)

    ;

    private final String activityName;

    private final Double ratio;

    LifeActivity(String activityName, Double ratio) {
        this.activityName = activityName;
        this.ratio = ratio;
    }

    public String getActivityName() {
        return activityName;
    }

    public Double getRatio() {
        return ratio;
    }
}
