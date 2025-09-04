package tg.fitnessbot.services.impl;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.fitnessbot.models.ProfilePhoto;
import tg.fitnessbot.models.User;
import tg.fitnessbot.repositories.ProfilePhotoRepository;
import tg.fitnessbot.repositories.UserRepository;
import tg.fitnessbot.services.ProfilePhotoService;

@Service
public class ProfilePhotoServiceImpl implements ProfilePhotoService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfilePhotoRepository profilePhotoRepository;

    @Override
    public boolean setPhoto(Long userId, String fileId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }
        ProfilePhoto photo = ProfilePhoto.builder().fileId(fileId).user(user).build();
        profilePhotoRepository.save(photo);
        return true;
    }

    @Override
    public String getPhotoFileId(Long userId) {
        ProfilePhoto photo = profilePhotoRepository.findByUser(userRepository.findById(userId).orElse(null));
        if (photo != null) {
            return photo.getFileId();
        }
        return null;
    }
}
