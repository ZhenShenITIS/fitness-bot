package tg.fitnessbot.telegram.callback;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.util.HashMap;

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
