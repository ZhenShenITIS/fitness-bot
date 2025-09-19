package tg.fitnessbot.services.impl;

import org.apache.commons.io.IOUtils;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.fitnessbot.config.TelegramConfig;
import tg.fitnessbot.constants.MessageText;
import tg.fitnessbot.services.FileService;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;


@Service
public class FileServiceImpl implements FileService {

    @Autowired
    TelegramConfig telegramConfig;

    @Override
    public File getVoiceFile(Message message) {
        String fileId = message.getVoice().getFileId();
        String filePath = getFilePath(fileId);
        if (filePath != null) {
            return downloadFile(filePath);
        } return null;
    }

    @Override
    public File getAudioFile(Message message) {
        String fileId = message.getAudio().getFileId();
        String filePath = getFilePath(fileId);
        if (filePath != null) {
            return downloadFile(filePath);
        } return null;
    }

    private String getFilePath (String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(new HttpHeaders());
        String requestUri = telegramConfig.getServiceFileInfoUri().formatted(telegramConfig.getBotToken(), fileId);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(requestUri, HttpMethod.GET, request, String.class);
        } catch (RuntimeException e) {
            throw new RuntimeException(MessageText.FILE_TOO_BIG.getMessageText());
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = new JSONObject(response.getBody());
            return String.valueOf(jsonObject.getJSONObject("result").getString("file_path"));
        }
        return null;
    }

    private File downloadFile (String filePah) {
        String requestUri = telegramConfig.getServiceFileStorageUri().formatted(telegramConfig.getBotToken(), filePah);
        URL url = null;
        try {
            url = new URL(requestUri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        File tempFile;
        try {
            tempFile = File.createTempFile("voice-", ".ogg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (InputStream inputStream = url.openStream();
             FileOutputStream fileOutputStream = new FileOutputStream(tempFile)
        ) {
            // Это хороший метод, скачивание происходит в потоке и не грузит оперативку
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }
}
