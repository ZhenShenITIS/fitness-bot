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

import static tg.fitnessbot.constants.CommandName.UPDATE_ACTIVITY;

@Component
public class UpdateActivityCommand implements Command {
    CommandName commandName = CommandName.UPDATE_ACTIVITY;

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
            String cmdText = message.getText().substring(UPDATE_ACTIVITY.getCommandName().length()).trim().replaceAll(",", ".");
            String[] lines = cmdText.split("\n");
            int counterOfUpdate = 0;
            int counterOfAdd = 0;
            String textToSend = "";

            if (lines.length > 0
                    && (lines[0].split(" ").length >= 2)
                    && (lines[0].split(" ")[lines[0].split(" ").length - 1].charAt(0) <= '9' && lines[0].split(" ")[lines[0].split(" ").length - 1].charAt(0) >= '0')) {
                for (String line : lines) {
                    String[] activity = line.trim().split(" ");
                    int len = activity.length;

                    ActivityForm activityForm;
                    String activityName = "";
                    for (int j = 0; j < len - 1; j++) {
                        activityName = activityName + activity[j] + " ";
                    }
                    activityName = activityName.trim().toLowerCase();
                    try {
                        activityForm = ActivityForm
                                .builder()
                                .name(activityName)
                                .met(Double.parseDouble(activity[len - 1]))
                                .build();
                    } catch (NumberFormatException e) {
                        textToSend = textToSend + String.format(MessageText.WRONG_ACTIVITY_LINE_DB.getMessageText(), line);
                        continue;
                    }

                    // Добавлен счетчик, так как при добавлении большого количества еды, бот не может отправить какие продукты не были добавлены
                    // Из-за ограничений на размер сообщения
                    if (activityService.updateActivity(activityForm)) {
                        if (counterOfUpdate < IntegerConstants.NUMBER_OF_SUCCESS_LINES.getValue()) {
                            textToSend = textToSend + String.format(MessageText.SUCCESS_UPDATE_ACTIVITY.getMessageText(), line);
                            counterOfUpdate++;
                        } else if (IntegerConstants.NUMBER_OF_SUCCESS_LINES.getValue().equals(counterOfUpdate)) {
                            textToSend = textToSend + MessageText.TO_BE_CONTINUED.getMessageText();
                            counterOfUpdate++;
                        }
                    } else {
                        if (activityService.addActivity(activityForm)) {
                            if (counterOfAdd < IntegerConstants.NUMBER_OF_SUCCESS_LINES.getValue()) {
                                textToSend = textToSend + String.format(MessageText.ACTIVITY_NOT_FOUND_AND_ADD.getMessageText(), activityName);
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
}
