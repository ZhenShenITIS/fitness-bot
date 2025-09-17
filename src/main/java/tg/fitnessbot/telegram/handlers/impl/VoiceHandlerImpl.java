package tg.fitnessbot.telegram.handlers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.constants.CommandName;
import tg.fitnessbot.constants.IntegerConstants;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.constants.StringConstants;
import tg.fitnessbot.services.AI.AudioTranscriptionService;
import tg.fitnessbot.services.AI.LLMService;
import tg.fitnessbot.services.FileService;
import tg.fitnessbot.telegram.command.impl.CalculateActivityCommand;
import tg.fitnessbot.telegram.command.impl.CalculateFoodCommand;
import tg.fitnessbot.telegram.handlers.VoiceHandler;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;

@Component
public class VoiceHandlerImpl implements VoiceHandler {
    @Autowired
    FileService fileService;

    @Autowired
    LLMService llmService;

    @Autowired
    AudioTranscriptionService audioTranscriptionService;

    @Autowired
    CalculateFoodCommand calculateFoodCommand;

    @Autowired
    CalculateActivityCommand calculateActivityCommand;

    @Override
    public BotApiMethod<?> answerMessage(Message message, SpringWebhookBot springWebhookBot) {
        String textToSend = "";
        java.io.File file = fileService.getVoiceFile(message);
        MultimediaObject object = new MultimediaObject(file);
        long duration;
        try {
            duration = object.getInfo().getDuration();
        } catch (EncoderException e) {
            throw new RuntimeException(e);
        }
        if (duration > IntegerConstants.MAX_VOICE_DURATION.getValue()) {
            textToSend = MessageText.VOICE_IS_TOO_LONG.getMessageText();
            file.delete();
        } else {
            String transcription = audioTranscriptionService.transcribeAudio(file);
            textToSend = llmService.processAudio(transcription);

            String headOfAnswer = textToSend.split("\n")[0];
            if (headOfAnswer.equals(StringConstants.FOOD.getValue())) {
                String msgt = CommandName.CALCULATE_FOOD.getCommandName() + textToSend.substring(StringConstants.FOOD.getValue().length());
                message.setText(msgt);
                return calculateFoodCommand.handleCommand(message, springWebhookBot);
            } else if (headOfAnswer.equals(StringConstants.ACTIVITY.getValue())) {
                String msgt = CommandName.CALCULATE_ACTIVITY.getCommandName() + textToSend.substring(StringConstants.ACTIVITY.getValue().length());
                message.setText(msgt);
                return calculateActivityCommand.handleCommand(message, springWebhookBot);
            } else {
                textToSend = MessageText.NO_FOOD_OR_ACTIVITY_IN_VOICE.getMessageText();
            }

        }
        if (textToSend.length() > 4090) {
            textToSend = textToSend.substring(0, 4000) + "...";
        }

        try {
            springWebhookBot.execute(SendMessage.builder().text(textToSend).chatId(message.getChatId()).build());
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
