package tg.fitnessbot.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class VoskClientImpl implements VoskClient {
    final private String uri = "http://5.45.109.247:8086/stt";
    // 8086 - big
    // 8087 - small

    @Override
    public ResponseEntity<String> getResponse(byte[] data) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "audio/x-wav");
        HttpEntity<byte[]> request = new HttpEntity<>(data, headers);
        return restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
    }
}
