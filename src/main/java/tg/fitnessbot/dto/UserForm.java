package tg.fitnessbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tg.fitnessbot.constants.Gender;
import tg.fitnessbot.constants.LifeActivity;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserForm {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Double weight;
    private Integer height;
    private Gender gender;
    private LocalDate birthday;
    private LifeActivity lifeActivity;
}
