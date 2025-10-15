package tg.fitnessbot.telegram.callback.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.IntegerConstants;
import tg.fitnessbot.constants.Payments;
import tg.fitnessbot.telegram.callback.Callback;

import java.util.List;
@Component
public class BasicSubscriptionCallback implements Callback {
    CallbackName callbackName = CallbackName.BASIC_SUBSCRIPTION;
    @Override
    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery, SpringWebhookBot springWebhookBot) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Payments payments = Payments.BASIC_SUBSCRIPTION;
        SendInvoice sendInvoice = SendInvoice.builder()
                .chatId(chatId)
                .title(payments.getTitle())
                .description(payments.getDescription())
                .payload(payments.getName())
                .providerToken("")
                .currency("XTR")
                .prices(List.of(new LabeledPrice(payments.getLabel(), IntegerConstants.BASE_SUBSCRIPTION_COST.getValue())))
                .needEmail(false)
                .needName(false)
                .needPhoneNumber(false)
                .build();

        try {
            springWebhookBot.execute(sendInvoice);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message, SpringWebhookBot springWebhookBot) {
        return null;
    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
