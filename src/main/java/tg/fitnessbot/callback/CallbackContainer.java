package tg.fitnessbot.callback;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.springframework.stereotype.Component;
import tg.fitnessbot.command.Command;
import tg.fitnessbot.command.UnknownCommand;
import tg.fitnessbot.constants.CallbackName;

import java.util.HashMap;

import static tg.fitnessbot.constants.CallbackName.*;

@Component
public class CallbackContainer {
    private final HashMap<String, Callback> callbacks;

    public CallbackContainer(Callback[] callbackArray) {
        callbacks = new HashMap<>();
        for (Callback callback : callbackArray) {
            callbacks.put(callback.getCallback().getCallbackName(), callback);
        }
    }

    public Callback retrieveCallback(String callbackIdentifier) {
        return callbacks.get(callbackIdentifier);
    }
}
