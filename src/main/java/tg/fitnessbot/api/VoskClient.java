package tg.fitnessbot.api;

import org.springframework.http.ResponseEntity;

import java.io.File;

public interface VoskClient {
    ResponseEntity<String> getResponse (File data);
}
