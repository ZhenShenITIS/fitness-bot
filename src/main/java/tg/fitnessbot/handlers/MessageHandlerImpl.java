package tg.fitnessbot.handlers;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.callback.CallbackContainer;
import tg.fitnessbot.command.CommandContainer;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CallbackName;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.dto.FoodForm;
import tg.fitnessbot.services.FoodServiceImpl;

import java.text.DecimalFormat;
import java.util.HashMap;

@Data
@Component
public class MessageHandlerImpl implements MessageHandler {

    private final DecimalFormat decimalFormat = new DecimalFormat( "#.#" );


    private final CommandContainer commandContainer;


    private final CallbackContainer callbackContainer;


    private final FoodServiceImpl foodService;


    private final TelegramConfig telegramConfig;


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
                return callbackContainer.retrieveCallback(state.getCallbackName()).answerMessage(message);
            } else if (message.getChat().isUserChat()){
                // TODO Реализовать логику работы сообщения, не содержащего команды
                return SendMessage.builder().chatId(message.getChatId()).text("Вы не ввели никакой команды").build();

            }
        }
        return null;
    }

    // TODO Вынести метод в отдельный класс
    // Хочу убрать сервисы в этом (MessageHandler) классе
    // и чтоб с сервисами взаимодействовали только классы команд
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
