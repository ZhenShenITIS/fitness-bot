package tg.fitnessbot.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.constants.CommandName;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.dto.FoodForm;
import tg.fitnessbot.services.FoodService;

import java.text.DecimalFormat;
import java.util.HashMap;

import static tg.fitnessbot.constants.CommandName.CALCULATE_ACTIVITY;
import static tg.fitnessbot.constants.CommandName.CALCULATE_FOOD;
@Component
public class CalculateFoodCommand implements Command {
   CommandName commandName = CommandName.CALCULATE_FOOD;

    private final DecimalFormat decimalFormat = new DecimalFormat( "#.#" );

    @Autowired
    FoodService foodService;

    @Override
    public CommandName getCommand() {
        return commandName;
    }

    @Override
    public BotApiMethod<?> handleCommand(Message message) {
        String msgText = message.getText().substring(CALCULATE_FOOD.getCommandName().length()).trim().replaceAll(",", ".");
        String textToSend = "";
        String[] lines = msgText.split("\n");
        HashMap<String, Double> foods = new HashMap<>();
        for (int i = 0; i < lines.length; i++) {
            String[] lineParts = lines[i].split(" ");
            if (lineParts.length >= 2
                    && (lineParts[lineParts.length-1].charAt(0) <= '9' && lineParts[lineParts.length-1].charAt(0) >= '1')) {
                String foodName = "";
                for (int j = 0; j < lineParts.length - 1; j++) {
                    foodName = foodName + lineParts[j] + " ";
                }
                foodName = foodName.trim().toLowerCase();
                try {
                    foods.put(foodName, Double.parseDouble(lineParts[lineParts.length - 1]));
                } catch (NumberFormatException ignored){}
            }
        }
        HashMap<FoodForm, Double> foodForms = foodService.getFoodByName(foods);
        if (!foodForms.isEmpty()) {
            textToSend = textToSend + MessageText.SUCCESS_RECOGNIZE_FOOD.getMessageText();
            for (FoodForm key : foodForms.keySet()) {
                textToSend = textToSend + key.getName() + "\n";
            }

            FoodForm foodForm = foodService.calculateFood(foodForms);

            textToSend = textToSend+String.format(MessageText.FOOD_STAT.getMessageText(),
                    decimalFormat.format(foodForm.getKcal()),
                    decimalFormat.format(foodForm.getProtein()),
                    decimalFormat.format(foodForm.getFat()),
                    decimalFormat.format(foodForm.getCarbohydrates()));
        }

        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(textToSend)
                .build();
    }
}
