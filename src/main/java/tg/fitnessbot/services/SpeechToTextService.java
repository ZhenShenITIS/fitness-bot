package tg.fitnessbot.services;

public interface SpeechToTextService {
    // TODO Подумать в каком виде передавать данные в этот метод
    String convertSpeechToText(byte[] audio);
}
