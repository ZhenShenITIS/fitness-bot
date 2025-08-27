package tg.fitnessbot.api;

import org.springframework.http.ResponseEntity;

public interface OpenAIClient {
    ResponseEntity<String> getResponse (String data);
}
