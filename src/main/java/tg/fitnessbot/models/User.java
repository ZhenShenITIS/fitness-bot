package tg.fitnessbot.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tg.fitnessbot.constants.Gender;
import tg.fitnessbot.constants.LifeActivity;

import java.time.LocalDate;


@Entity
@Table(name = "bot_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Double weight;
    private Integer height;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birthday;
    // TODO Вопрос по новому полю у класса модели
    // Такая ситуация: в ходе разработки понял, что
    // нужно ещё такое поле как физическая активность
    // человека, соответственно надо теперь во многих
    // местах код менять где эти параметры использовались
    // (банально сохранение в БД). Возможно есть
    // какой-то подход в написании кода, который
    // поможет этого избежать
    private LifeActivity lifeActivity;

    // TODO Добавить поля наличия премиума и количества оставшихся бесплатных использования премиум функций

}
