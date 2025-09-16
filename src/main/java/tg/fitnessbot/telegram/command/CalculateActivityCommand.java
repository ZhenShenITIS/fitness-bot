package tg.fitnessbot.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.constants.CommandName;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.dto.ActivityForm;
import tg.fitnessbot.services.ActivityService;
import tg.fitnessbot.services.UserService;

import java.text.DecimalFormat;
import java.util.HashMap;

import static tg.fitnessbot.constants.CommandName.CALCULATE_ACTIVITY;

@Component
public class CalculateActivityCommand implements Command{
    CommandName commandName = CommandName.CALCULATE_ACTIVITY;

    private final DecimalFormat decimalFormat = new DecimalFormat( "#.#" );

    @Autowired
    ActivityService activityService;

    @Autowired
    UserService userService;

    @Override
    public CommandName getCommand() {
        return commandName;
    }

    @Override
    public BotApiMethod<?> handleCommand(Message message, SpringWebhookBot springWebhookBot) {
        Double weight = userService.getUserByID(message.getFrom().getId()).getWeight();
        if (weight == null) {
            // TODO добавить реплей маркап с кнопкой /start
            return SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text(MessageText.NO_WEIGHT.getMessageText())
                    .build();
        }
        String msgText = message.getText().substring(CALCULATE_ACTIVITY.getCommandName().length()).trim().replaceAll(",", ".");
        String textToSend = "";
        String[] lines = msgText.split("\n");
        HashMap<String, Double> activities = new HashMap<>();
        for (int i = 0; i < lines.length; i++) {
            String[] lineParts = lines[i].split(" ");
            if (lineParts.length >= 2
                    && (lineParts[lineParts.length-1].charAt(0) <= '9' && lineParts[lineParts.length-1].charAt(0) >= '1')) {
                String activityName = "";
                for (int j = 0; j < lineParts.length - 1; j++) {
                    activityName = activityName + lineParts[j] + " ";
                }
                activityName = activityName.trim().toLowerCase();
                try {
                    activities.put(activityName, (Double) Double.parseDouble(lineParts[lineParts.length - 1]) );
                } catch (NumberFormatException ignored){}
            }
        }
        HashMap<ActivityForm, Double> activityForms = activityService.getActivityByName(activities);
        // TODO убрать отладочный вывод
        System.out.println(activityForms);
        if (!activityForms.isEmpty()) {
            textToSend = textToSend + MessageText.SUCCESS_RECOGNIZE_ACTIVITY.getMessageText();
            for (ActivityForm key : activityForms.keySet()) {
                textToSend = textToSend + key.getName() + "\n";
            }

            Double kcal = activityService.calculateActivity(activityForms, weight);

            textToSend = textToSend+String.format(MessageText.ACTIVITY_STAT.getMessageText(),
                    decimalFormat.format(kcal));
        }

        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(textToSend)
                .build();
    }
}
