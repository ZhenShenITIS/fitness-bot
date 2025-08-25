package tg.fitnessbot.services;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    byte[] getVoiceFile(Message message);
}
