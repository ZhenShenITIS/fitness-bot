package tg.fitnessbot.api.impl;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tg.fitnessbot.api.VoskClient;

import java.io.*;

@Component
public class VoskClientImpl implements VoskClient {
    final private String uri = "http://5.45.109.247:8087/stt";
    // final private String uri = "http://5.45.109.247:2700";
    // 8086 - big
    // 8087 - small

    @Override
    public ResponseEntity<String> getResponse(File data) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "audio/x-wav");
        HttpEntity<FileSystemResource> request = new HttpEntity<>(new FileSystemResource(data), headers);
        var answer = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        data.delete();
        return answer;
    }
}
