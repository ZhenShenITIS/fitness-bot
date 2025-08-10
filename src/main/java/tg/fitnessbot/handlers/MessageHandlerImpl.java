package tg.fitnessbot.handlers;

import lombok.Data;
import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.callback.CallbackContainer;
import tg.fitnessbot.command.AddFoodCommand;
import tg.fitnessbot.command.CommandContainer;
import tg.fitnessbot.command.StartCommand;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.dto.FoodForm;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.services.FoodServiceImpl;
import tg.fitnessbot.services.SignUpService;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class MessageHandlerImpl implements MessageHandler {

    private final DecimalFormat decimalFormat = new DecimalFormat( "#.#" );



    @Autowired
    CommandContainer commandContainer;

    @Autowired
    CallbackContainer callbackContainer;

    @Autowired
    FoodServiceImpl foodService;

    @Autowired
    TelegramConfig telegramConfig;


    @Override
    public BotApiMethod<?> answerMessage(Message message) {
        if (message.hasText()) {
            CallbackName state = telegramConfig.getUserStateMap().getOrDefault(message.getFrom().getId(), CallbackName.NONE);
            String[] msgParts = message.getText().split(" ");
            if (message.getText().startsWith("/")) {
                String commandIdentifier = message.getText().split(" ")[0].split("\n")[0].split(telegramConfig.getBotName())[0].toLowerCase();
                return commandContainer.retrieveCommand(commandIdentifier).handleCommand(message);
                // TODO Поправить распознавание команды на подсчет каллорий
            } else if (msgParts.length >= 2
                        && (msgParts[msgParts.length-1].charAt(0) <= '9' && msgParts[msgParts.length-1].charAt(0) >= '1')){
                return calculateFood(message);

            } else if (!state.equals(CallbackName.NONE)) {
                SendMessage[] messages = new SendMessage[2];
                return callbackContainer.retrieveCallback(state.getCallbackName()).answerMessage(message);
            } else if (message.getChat().isUserChat()){
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
            textToSend = textToSend + "Удалось распознать следующие продукты: \n";
            for (FoodForm key : foodForms.keySet()) {
                textToSend = textToSend + key.getName() + "\n";
            }

            FoodForm foodForm = foodService.calculateFood(foodForms);

            textToSend = textToSend+"\nОбщая каллорийность введённых продуктов: " + decimalFormat.format(foodForm.getKcal()) + "\n"
                    +"Общее количество белка: " + decimalFormat.format(foodForm.getProtein()) + "\n"
                    +"Общее количество жиров: " + decimalFormat.format(foodForm.getFat()) + "\n"
                    +"Общее количество углеводов: " + decimalFormat.format(foodForm.getCarbohydrates()) + "\n";
        }

        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(textToSend)
                .build();
    }
}
