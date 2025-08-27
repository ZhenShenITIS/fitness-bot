package tg.fitnessbot.services;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;

public interface FileService {
    File getVoiceFile(Message message);

    File getAudioFile(Message message);
}
