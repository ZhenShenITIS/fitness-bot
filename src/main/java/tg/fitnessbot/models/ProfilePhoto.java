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
@IdClass(User.class)
public class ProfilePhoto {

    private String fileId;

    @Id
    @OneToOne @JoinColumn(name = "bot_user_id")
    private User user;

}
