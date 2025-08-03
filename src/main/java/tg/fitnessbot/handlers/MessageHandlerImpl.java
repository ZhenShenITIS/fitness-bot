package tg.fitnessbot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.command.AddFoodCommand;
import tg.fitnessbot.command.CommandContainer;
import tg.fitnessbot.command.StartCommand;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.dto.FoodForm;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.FoodServiceImpl;
import tg.fitnessbot.services.SignUpService;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;

@Component
public class MessageHandlerImpl implements MessageHandler {

    private final DecimalFormat decimalFormat = new DecimalFormat( "#.#" );

    @Autowired
    CommandContainer commandContainer;

    @Autowired
    FoodServiceImpl foodService;


    @Override
    public BotApiMethod<?> answerMessage(Message message) {
        if (message.hasText()) {
            String[] msgParts = message.getText().split(" ");
            if (message.getText().startsWith("/")) {
                String commandIdentifier = message.getText().split(" ")[0].split("\n")[0].toLowerCase();
                return commandContainer.retrieveCommand(commandIdentifier).handleCommand(message);
                // TODO Поправить распознавание команды на подсчет каллорий
            } else if (msgParts.length >= 2
                        && (msgParts[msgParts.length-1].charAt(0) <= '9' && msgParts[msgParts.length-1].charAt(0) >= '1')){
                return calculateFood(message);

            } else {
                // TODO Реализовать логику работы сообщения, не содержащего команды
                return SendMessage.builder().chatId(message.getChatId()).text("Вы не ввели никакой команды").build();

            }
        }
        return null;
    }

    public BotApiMethod<?> calculateFood (Message message) {
        String textToSend = "";
        String msgText = message.getText();
        String[] lines = msgText.split("\n");
        HashMap<String, Double> foods = new HashMap<>();
        for (int i = 0; i < lines.length; i++) {
            String[] lineParts = lines[i].split(" ");
            if (lineParts.length == 2
                    && (lineParts[1].charAt(0) <= '9' && lineParts[1].charAt(0) >= '1')) {
                try {
                    foods.put(lineParts[0], Double.parseDouble(lineParts[1]));
                } catch (NumberFormatException e) {
                    textToSend = textToSend + "Неправильный ввод строки №" + (i + 1) + "!\n";
                }
            } else {
                textToSend = textToSend + "Неправильный ввод строки №" + (i + 1) + "!\n";
            }
        }
        FoodForm foodForm = null;
        try {
            foodForm = foodService.calculateFood(foods);
        } catch (NullPointerException e) {
            return SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text("Не удалось найти какой-то из введённых продуктов в базе, попробуйте ещё раз")
                    .build();
        }
        textToSend = textToSend+"Общая каллорийность введённых продуктов: " + decimalFormat.format(foodForm.getKcal()) + "\n"
                +"Общее количество белка: " + decimalFormat.format(foodForm.getProtein()) + "\n"
                +"Общее количество жиров: " + decimalFormat.format(foodForm.getFat()) + "\n"
                +"Общее количество углеводов: " + decimalFormat.format(foodForm.getCarbohydrates()) + "\n";

        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(textToSend)
                .build();
    }
}
