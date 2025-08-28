package tg.fitnessbot.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenAIClientImpl implements OpenAIClient {
    @Value("${OPEN_AI_TOKEN}")
    private String token;

    private final String uri = "https://api.openai.com/v1/responses";


    @Override
    public ResponseEntity<String> getResponse(String data) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);
        OpenAIRequest body = new OpenAIRequest("gpt-5-nano", data);
        HttpEntity<OpenAIRequest> request = new HttpEntity<>(body, headers);
        return restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
    }
}
