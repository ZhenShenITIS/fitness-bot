package tg.fitnessbot.command;

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

import static tg.fitnessbot.constants.CommandName.ADD_FOOD;
import static tg.fitnessbot.constants.CommandName.DELETE_FOOD;

@Component
public class DeleteFoodCommand implements Command{
    CommandName commandName = CommandName.DELETE_FOOD;

    @Autowired
    FoodService foodService;

    @Autowired
    UserUtil userUtil;

    @Override
    public CommandName getCommand() {
        return commandName;
    }

    @Override
    public BotApiMethod<?> handleCommand(Message message) {
        if (userUtil.isAdmin(message.getFrom().getId())){
            String cmdText = message.getText().substring(DELETE_FOOD.getCommandName().length()).trim().replaceAll(",", ".");
            String[] lines = cmdText.split("\n");
            String textToSend = "";
            int counter = 0;
            for (String line : lines) {
                FoodForm food = FoodForm.builder().name(line.trim().toLowerCase()).build();
                if (foodService.deleteFood(food)) {
                    if (counter < IntegerConstants.NUMBER_OF_SUCCESS_LINES.getValue()) {
                        textToSend = textToSend + String.format(MessageText.SUCCESS_DELETE_FOOD.getMessageText(), line);
                    } else if (IntegerConstants.NUMBER_OF_SUCCESS_LINES.getValue().equals(counter)) {
                        textToSend = textToSend + MessageText.TO_BE_CONTINUED;
                    }
                } else {
                    textToSend = textToSend + String.format(MessageText.FOOD_NOT_FOUND.getMessageText(), line);
                }
            }

            return SendMessage.builder().text(textToSend).chatId(message.getChatId()).build();
        } else {
            SendMessage messageToSend = SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text(MessageText.NOT_ADMIN.getMessageText())
                    .build();
            return messageToSend;
        }
    }
}
