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

import java.util.Arrays;
import java.util.HashMap;

@Component
public class MessageHandlerImpl implements MessageHandler {

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
            } else if (msgParts.length >= 2
                        && (msgParts[1].charAt(0) <= '9' && msgParts[1].charAt(0) >= '1')){
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
        FoodForm foodForm = foodService.calculateFood(foods);
        textToSend = textToSend+"Общая каллорийность введённых продуктов: " + foodForm.getKcal() + "\n"
                +"Общее количество белка: " + foodForm.getProtein() + "\n"
                +"Общее количество жиров: " + foodForm.getFat() + "\n"
                +"Общее количество углеводов: " + foodForm.getCarbohydrates() + "\n";

        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(textToSend)
                .build();
    }
}
