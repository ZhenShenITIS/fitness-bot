package tg.fitnessbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.CommandName;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.dto.FoodForm;
import tg.fitnessbot.services.FoodServiceImpl;

import java.util.Arrays;

import static tg.fitnessbot.constants.CommandName.ADD_FOOD;

@Component
public class AddFoodCommand implements Command{

    CommandName commandName = ADD_FOOD;
    @Autowired
    TelegramConfig telegramConfig;

    @Autowired
    FoodServiceImpl foodService;

    @Override
    public BotApiMethod<?> handleCommand(Message message) {
        // TODO Сделать более красивую проверку админа
        if (telegramConfig.getAdmins().length > 0 && Arrays.stream(telegramConfig.getAdmins()).filter(l -> l.equals(message.getChat().getId())).toArray().length > 0) {
            // TODO Реализовать функционал добавления еды в базу
            String cmdText = message.getText().substring(ADD_FOOD.getCommandName().length()).trim().replaceAll(",", ".");
            String[] lines = cmdText.split("\n");

            String textToSend = "";

            if (lines.length > 0
                    && (lines[0].split(" ").length >= 5)
                    && (lines[0].split(" ")[lines[0].split(" ").length - 4].charAt(0) <= '9' && lines[0].split(" ")[lines[0].split(" ").length - 4].charAt(0) >= '0')
                    && (lines[0].split(" ")[lines[0].split(" ").length - 3].charAt(0) <= '9' && lines[0].split(" ")[lines[0].split(" ").length - 3].charAt(0) >= '0')
                    && (lines[0].split(" ")[lines[0].split(" ").length - 2].charAt(0) <= '9' && lines[0].split(" ")[lines[0].split(" ").length - 2].charAt(0) >= '0')
                    && (lines[0].split(" ")[lines[0].split(" ").length - 1].charAt(0) <= '9' && lines[0].split(" ")[lines[0].split(" ").length - 1].charAt(0) >= '0')) {
                for (int i = 0; i < lines.length; i++) {
                    String[] food = lines[i].split(" ");
                    int len = food.length;

                    FoodForm foodForm;
                    String foodName = "";
                    for (int j = 0; j < len - 4; j++) {
                        foodName = foodName + food[j] + " ";
                    }
                    foodName = foodName.trim();
                    try {
                         foodForm = FoodForm
                                .builder()
                                .name(foodName)
                                .kcal(Double.parseDouble(food[len - 4]))
                                .protein(Double.parseDouble(food[len - 3]))
                                .fat(Double.parseDouble(food[len - 2]))
                                .carbohydrates(Double.parseDouble(food[len - 1]))
                                .build();
                    } catch (NumberFormatException e) {
                        textToSend = textToSend + String.format(MessageText.WRONG_FOOD_LINE_DB.getMessageText(), lines[i]);
                        continue;
                    }
                    if (foodService.addFood(foodForm)) {
                        textToSend = textToSend + String.format(MessageText.SUCCESS_ADD_FOOD.getMessageText(), lines[i]);
                    } else {
                        textToSend = textToSend + String.format(MessageText.ALREADY_EXIST_FOOD.getMessageText(), foodName);
                    }

                }

            } else {
                return SendMessage
                        .builder()
                        .chatId(message.getChatId())
                        .text(MessageText.WRONG_INPUT.getMessageText())
                        .build();
            }
            SendMessage msgToSend = SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text(textToSend)
                    .build();
            return msgToSend;
        } else {
            SendMessage messageToSend = SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text(MessageText.NOT_ADMIN.getMessageText())
                    .build();
            return messageToSend;
        }
    }

    @Override
    public CommandName getCommand() {
        return commandName;
    }
}
