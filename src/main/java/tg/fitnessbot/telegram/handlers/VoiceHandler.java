package tg.fitnessbot.telegram.handlers;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface VoiceHandler {
    BotApiMethod<?> answerMessage(Message message);
}
