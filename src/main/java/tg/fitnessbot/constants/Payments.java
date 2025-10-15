package tg.fitnessbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Payments {
    BASIC_SUBSCRIPTION("basic_subscription", "Премиум-подписка", "Доступ к премиум-функциям на 1 месяц", "1 месяц премиум")
    ;


    private final String name;
    private final String title;
    private final String description;
    private final String label;
}
