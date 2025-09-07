package tg.fitnessbot.telegram.handlers;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.starter.SpringWebhookBot;

public interface VoiceHandler {
    BotApiMethod<?> answerMessage(Message message, SpringWebhookBot springWebhookBot);
}
