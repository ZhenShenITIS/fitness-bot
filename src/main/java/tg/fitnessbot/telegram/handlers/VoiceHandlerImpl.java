package tg.fitnessbot.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Voice;

@Component
public class VoiceHandlerImpl implements VoiceHandler {
    @Override
    public BotApiMethod<?> answerMessage(Message message) {
        message.getVoice().getFileId();
        Voice voice = message.getVoice();
        return null;
    }
}
