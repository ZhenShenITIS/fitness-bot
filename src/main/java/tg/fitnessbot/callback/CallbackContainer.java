package tg.fitnessbot.callback;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.springframework.stereotype.Component;
import tg.fitnessbot.command.Command;
import tg.fitnessbot.command.UnknownCommand;
import tg.fitnessbot.constants.CallbackName;

import static tg.fitnessbot.constants.CallbackName.*;

@Component
public class CallbackContainer {
    private final ImmutableMap<String, Callback> callbacks;

    public CallbackContainer(UpdateBirthdayCallback updateBirthdayCallback,
                             UpdateGenderCallback updateGenderCallback,
                             UpdateHeightCallback updateHeightCallback,
                             UpdateWeightCallback updateWeightCallback,
                             NoneCallback noneCallback) {
        callbacks = ImmutableMap.<String, Callback>builder()
                .put(UPDATE_BIRTHDAY.getCallbackName(), updateBirthdayCallback)
                .put(UPDATE_GENDER.getCallbackName(), updateGenderCallback)
                .put(UPDATE_HEIGHT.getCallbackName(), updateHeightCallback)
                .put(UPDATE_WEIGHT.getCallbackName(), updateWeightCallback)
                .put(NONE.getCallbackName(), noneCallback)
                .build();
    }

    public Callback retrieveCallback(String callbackIdentifier) {
        return callbacks.get(callbackIdentifier);
    }
}
