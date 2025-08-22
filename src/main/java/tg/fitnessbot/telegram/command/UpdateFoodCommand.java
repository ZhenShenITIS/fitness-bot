package tg.fitnessbot.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.constants.CommandName;
import tg.fitnessbot.constants.IntegerConstants;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.dto.FoodForm;
import tg.fitnessbot.services.FoodService;
import tg.fitnessbot.utils.UserUtil;

import static tg.fitnessbot.constants.CommandName.UPDATE_FOOD;

@Component
public class UpdateFoodCommand implements Command{
    CommandName commandName = CommandName.UPDATE_FOOD;

    @Autowired
    FoodService foodService;

    @Autowired
    UserUtil userUtil;

    @Override
    public BotApiMethod<?> handleCommand(Message message) {
        if (userUtil.isAdmin(message.getFrom().getId())){
            String cmdText = message.getText().substring(UPDATE_FOOD.getCommandName().length()).trim().replaceAll(",", ".");
            String[] lines = cmdText.split("\n");
            int counterOfUpdate = 0;
            int counterOfAdd = 0;
            String textToSend = "";

            if (lines.length > 0
                    && (lines[0].split(" ").length >= 5)
                    && (lines[0].split(" ")[lines[0].split(" ").length - 4].charAt(0) <= '9' && lines[0].split(" ")[lines[0].split(" ").length - 4].charAt(0) >= '0')
                    && (lines[0].split(" ")[lines[0].split(" ").length - 3].charAt(0) <= '9' && lines[0].split(" ")[lines[0].split(" ").length - 3].charAt(0) >= '0')
                    && (lines[0].split(" ")[lines[0].split(" ").length - 2].charAt(0) <= '9' && lines[0].split(" ")[lines[0].split(" ").length - 2].charAt(0) >= '0')
                    && (lines[0].split(" ")[lines[0].split(" ").length - 1].charAt(0) <= '9' && lines[0].split(" ")[lines[0].split(" ").length - 1].charAt(0) >= '0')) {
                for (String line : lines) {
                    String[] food = line.trim().split(" ");
                    int len = food.length;

                    FoodForm foodForm;
                    String foodName = "";
                    for (int j = 0; j < len - 4; j++) {
                        foodName = foodName + food[j] + " ";
                    }
                    foodName = foodName.trim().toLowerCase();
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
                        textToSend = textToSend + String.format(MessageText.WRONG_FOOD_LINE_DB.getMessageText(), line);
                        continue;
                    }

                    // Добавлен счетчик, так как при добавлении большого количества еды, бот не может отправить какие продукты не были добавлены
                    // Из-за ограничений на размер сообщения
                    if (foodService.updateFood(foodForm)) {
                        if (counterOfUpdate < IntegerConstants.NUMBER_OF_SUCCESS_LINES.getValue()) {
                            textToSend = textToSend + String.format(MessageText.SUCCESS_UPDATE_FOOD.getMessageText(), line);
                            counterOfUpdate++;
                        } else if (IntegerConstants.NUMBER_OF_SUCCESS_LINES.getValue().equals(counterOfUpdate)) {
                            textToSend = textToSend + MessageText.TO_BE_CONTINUED.getMessageText();
                            counterOfUpdate++;
                        }
                    } else {
                        if (foodService.addFood(foodForm)) {
                            if (counterOfAdd < IntegerConstants.NUMBER_OF_SUCCESS_LINES.getValue()) {
                                textToSend = textToSend + String.format(MessageText.FOOD_NOT_FOUND_AND_ADD.getMessageText(), foodName);
                                counterOfAdd++;
                            } else if (IntegerConstants.NUMBER_OF_SUCCESS_LINES.getValue().equals(counterOfAdd)) {
                                textToSend = textToSend + MessageText.TO_BE_CONTINUED.getMessageText();
                                counterOfAdd++;
                            }

                        }
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
