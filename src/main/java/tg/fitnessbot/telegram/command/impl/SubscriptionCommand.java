package tg.fitnessbot.telegram.command.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.CommandName;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.services.SubscriptionService;
import tg.fitnessbot.telegram.command.Command;

import java.util.List;
@Component

public class SubscriptionCommand implements Command {
    CommandName commandName = CommandName.SUBSCRIPTION;

    public SubscriptionCommand(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    SubscriptionService subscriptionService;
    @Override
    public BotApiMethod<?> handleCommand(Message message, SpringWebhookBot springWebhookBot) {
        String textToSend;
        Long userId = message.getFrom().getId();
        int dayOfPremium = subscriptionService.getDayOfPremium(userId);
        if (dayOfPremium == 0) {
            int trialAttempts = subscriptionService.getTrialAttempts(userId);
            textToSend = MessageText.SUBSCRIPTION_COMMAND_MESSAGE_TRIAL.getMessageText().formatted(trialAttempts);
        } else {
            textToSend = MessageText.SUBSCRIPTION_COMMAND_MESSAGE_PREMIUM.getMessageText().formatted(dayOfPremium);
        }
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(textToSend)
                .replyMarkup(InlineKeyboardMarkup
                        .builder()
                        .keyboardRow(List.of(
                                InlineKeyboardButton
                                        .builder()
                                        .text(MessageText.INLINE_BUTTON_BASIC_SUBSCRIPTION.getMessageText())
                                        .callbackData(CallbackName.BASIC_SUBSCRIPTION.getCallbackName()+":"+userId)
                                        .build()))
                        .build())
                .build();
        try {
            springWebhookBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public CommandName getCommand() {
        return commandName;
    }
}
