package tg.fitnessbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.dto.FoodForm;
import tg.fitnessbot.services.FoodServiceImpl;

import java.util.Arrays;

import static tg.fitnessbot.constants.CommandName.ADD_FOOD;

@Component
public class AddFoodCommand implements Command{
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
                    String foodName = "";
                    FoodForm foodForm;
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
                        textToSend = textToSend + "Ошибка преобразования еды в объект, для добавления в базу данных в строке №" + (i + 1) + "!\n";
                        continue;
                    }
                    if (foodService.addFood(foodForm)) {
                        textToSend = textToSend + "Успешно добавлена в базу данных еда в строке №" + (i + 1) + "\n";
                    } else {
                        textToSend = textToSend + "Еда с именем " + foodName + " уже существует в базе данных\n";
                    }

                }

            } else {
                return SendMessage
                        .builder()
                        .chatId(message.getChatId())
                        .text("Некорректный ввод!")
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
                    .text("Вы не являетесь админом!")
                    .build();
            return messageToSend;
        }
    }
}
