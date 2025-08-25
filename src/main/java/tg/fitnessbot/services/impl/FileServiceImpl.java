package tg.fitnessbot.services.impl;

import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.services.FileService;

public class FileServiceImpl implements FileService {
    @Override
    public byte[] getVoiceFile(Message message) {
        return new byte[0];

    }
}
