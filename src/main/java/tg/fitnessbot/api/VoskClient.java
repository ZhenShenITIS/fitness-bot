package tg.fitnessbot.api;

import org.springframework.http.ResponseEntity;

public interface VoskClient {
    ResponseEntity<String> getResponse (byte[] data);
}
