package tg.fitnessbot.models;

import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

public class ProfilePhoto {
    @Id
    private Long fileId;

    @OneToOne
    private Long userId;

}
