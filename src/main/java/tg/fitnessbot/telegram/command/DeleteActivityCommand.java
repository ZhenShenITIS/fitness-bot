package tg.fitnessbot.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import tg.fitnessbot.constants.CommandName;
import tg.fitnessbot.constants.IntegerConstants;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.dto.ActivityForm;
import tg.fitnessbot.services.ActivityService;
import tg.fitnessbot.utils.UserUtil;

import static tg.fitnessbot.constants.CommandName.DELETE_ACTIVITY;

@Component
public class DeleteActivityCommand implements Command {
    CommandName commandName = CommandName.DELETE_ACTIVITY;

    @Autowired
    UserUtil userUtil;

    @Autowired
    ActivityService activityService;

    @Override
    public CommandName getCommand() {
        return commandName;
    }

    @Override
    public BotApiMethod<?> handleCommand(Message message, SpringWebhookBot springWebhookBot) {
        if (userUtil.isAdmin(message.getFrom().getId())){
            String cmdText = message.getText().substring(DELETE_ACTIVITY.getCommandName().length()).trim().replaceAll(",", ".");
            String[] lines = cmdText.split("\n");
            String textToSend = "";
            int counter = 0;
            for (String line : lines) {
                ActivityForm activity = ActivityForm.builder().name(line.trim().toLowerCase()).build();
                if (activityService.deleteActivity(activity)) {
                    if (counter < IntegerConstants.NUMBER_OF_SUCCESS_LINES.getValue()) {
                        textToSend = textToSend + String.format(MessageText.SUCCESS_DELETE_ACTIVITY.getMessageText(), line);
                    } else if (IntegerConstants.NUMBER_OF_SUCCESS_LINES.getValue().equals(counter)) {
                        textToSend = textToSend + MessageText.TO_BE_CONTINUED;
                    }
                } else {
                    textToSend = textToSend + String.format(MessageText.ACTIVITY_NOT_FOUND.getMessageText(), line);
                }
            }

            return SendMessage.builder().text(textToSend).chatId(message.getChatId()).build();
        } else {
            SendMessage messageToSend = SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text(MessageText.NOT_ADMIN.getMessageText())
                    .build();
//            List<KeyboardButton> list = new ArrayList<>();
//            list.add(new KeyboardButton("м"));
//            list.add(new KeyboardButton("ж"));
//            messageToSend.setReplyMarkup(ReplyKeyboardMarkup
//                    .builder()
//                    .keyboardRow(new KeyboardRow(list))
//                    .keyboardRow(new KeyboardRow(list))
//                    .keyboardRow(new KeyboardRow(list))
//                    .keyboardRow(new KeyboardRow(list))
//                    .keyboardRow(new KeyboardRow(list))
//                    .build());
            return messageToSend;
        }
    }
}
