package tg.fitnessbot.telegram.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Voice;
import tg.fitnessbot.constants.CommandName;
import tg.fitnessbot.constants.IntegerConstants;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.constants.StringConstants;
import tg.fitnessbot.services.AI.AudioTranscriptionService;
import tg.fitnessbot.services.AI.LLMService;
import tg.fitnessbot.services.AI.impl.LLMServiceImpl;
import tg.fitnessbot.services.FileService;
import tg.fitnessbot.telegram.command.CalculateActivityCommand;
import tg.fitnessbot.telegram.command.CalculateFoodCommand;
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
    public BotApiMethod<?> answerMessage(Message message) {
        SendMessage msg;
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
            textToSend = llmService.processAudio(audioTranscriptionService.transcribeAudio(file));
            String headOfAnswer = textToSend.split("\n")[0];
            if (headOfAnswer.equals(StringConstants.FOOD.getValue())) {
                message.setText(CommandName.CALCULATE_FOOD.getCommandName() + textToSend.substring(StringConstants.FOOD.getValue().length()));
                return calculateFoodCommand.handleCommand(message);
            } else if (headOfAnswer.equals(StringConstants.ACTIVITY.getValue())) {
                message.setText(CommandName.CALCULATE_ACTIVITY.getCommandName() + textToSend.substring(StringConstants.ACTIVITY.getValue().length()));
                return calculateActivityCommand.handleCommand(message);
            } else {
                textToSend = MessageText.NO_FOOD_OR_ACTIVITY_IN_VOICE.getMessageText();
            }

        }
        if (textToSend.length() > 4090) {
            textToSend = textToSend.substring(0, 4000) + "...";
        }

        return SendMessage.builder().text(textToSend).chatId(message.getChatId()).build();
    }
}
