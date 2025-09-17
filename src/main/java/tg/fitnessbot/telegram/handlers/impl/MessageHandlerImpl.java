package tg.fitnessbot.telegram.handlers.impl;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.telegram.callback.impl.CallbackContainer;
import tg.fitnessbot.telegram.command.impl.CommandContainer;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.telegram.handlers.MessageHandler;
import tg.fitnessbot.telegram.handlers.VoiceHandler;

@Data
@Component
public class MessageHandlerImpl implements MessageHandler {

    private final CommandContainer commandContainer;

    private final CallbackContainer callbackContainer;

    private final TelegramConfig telegramConfig;

    private final VoiceHandler voiceHandler;

    // TODO Оптимизировать метод
    @Override
    public BotApiMethod<?> answerMessage(Message message, SpringWebhookBot springWebhookBot) {
        CallbackName state = telegramConfig.getUserStateMap().getOrDefault(message.getFrom().getId(), CallbackName.NONE);

        if (!state.equals(CallbackName.NONE)) {
            return callbackContainer.retrieveCallback(state.getCallbackName()).answerMessage(message, springWebhookBot);
        } else if (message.hasText()) {
            if (message.getText().startsWith("/")) {
                String commandIdentifier = message.getText().split(" ")[0].split("\n")[0].split(telegramConfig.getBotName())[0].toLowerCase();
                return commandContainer.retrieveCommand(commandIdentifier).handleCommand(message, springWebhookBot);
            } else if (message.getChat().isUserChat()){
                return SendMessage.builder().chatId(message.getChatId()).text(MessageText.NO_COMMAND_USER_CHAT.getMessageText()).build();
            }
        } else if (message.hasVoice()) {
            return voiceHandler.answerMessage(message, springWebhookBot);
        }

        return null;
    }
}
