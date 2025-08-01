package tg.fitnessbot.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tg.fitnessbot.constants.Gender;

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

}
