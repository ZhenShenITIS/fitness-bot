package tg.fitnessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.fitnessbot.models.ProfilePhoto;
import tg.fitnessbot.models.User;

public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, User> {
    ProfilePhoto findByUser(User user);
}
