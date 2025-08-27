package tg.fitnessbot.services.AI.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tg.fitnessbot.api.VoskClient;
import tg.fitnessbot.services.AI.AudioTranscriptionService;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class AudioTranscriptionServiceImpl implements AudioTranscriptionService {
    @Autowired
    VoskClient voskClient;

    @Override
    public String transcribeAudio(byte[] audio) {
        byte[] wavBytes = convert(audio);
        ResponseEntity<String> response = voskClient.getResponse(wavBytes);
        return response.getBody();
    }

    // TODO Оптимизировать данный метод
    private byte[] convert(byte[] data) {
        return data;

    }
}
