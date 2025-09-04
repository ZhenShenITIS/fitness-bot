package tg.fitnessbot.services;

public interface ProfilePhotoService {
    boolean setPhoto (Long userId, String fileId);

    String getPhotoFileId (Long userId);
}
