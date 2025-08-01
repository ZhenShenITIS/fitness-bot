package tg.fitnessbot.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.fitnessbot.handlers.UpdateHandler;

@RestController
@AllArgsConstructor
public class WebhookController {
    private final UpdateHandler updateHandler;

    @PostMapping("/webhook")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return updateHandler.onWebhookUpdateReceived(update);
    }
}