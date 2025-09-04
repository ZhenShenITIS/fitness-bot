package tg.fitnessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.fitnessbot.models.ProfilePhoto;
import tg.fitnessbot.models.User;

import java.util.List;

public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, String> {
    ProfilePhoto findByUser(User user);
}
