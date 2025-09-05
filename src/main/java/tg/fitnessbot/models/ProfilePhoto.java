package tg.fitnessbot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePhoto {
    // TODO Не совсем понимаю как работает id в этом классе
    @Id
    private Long id;
    private String fileId;
    @OneToOne
    @JoinColumn(name = "bot_user_id", referencedColumnName = "id")
    private User user;

}
