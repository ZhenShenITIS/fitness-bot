package tg.fitnessbot.callback;

import tg.fitnessbot.constants.CallbackName;

public abstract class AbstractCallback implements Callback {
    CallbackName callbackName;

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
