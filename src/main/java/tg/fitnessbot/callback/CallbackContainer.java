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
    private final ImmutableMap<String, Callback> callbacks;

    public CallbackContainer(Callback[] callbackArray) {
        HashMap<String, Callback> map = new HashMap<>();
        for (Callback callback : callbackArray) {
            map.put(callback.getCallback().getCallbackName(), callback);
        }
        callbacks = ImmutableMap.copyOf(map);
    }

    public Callback retrieveCallback(String callbackIdentifier) {
        return callbacks.get(callbackIdentifier);
    }
}
