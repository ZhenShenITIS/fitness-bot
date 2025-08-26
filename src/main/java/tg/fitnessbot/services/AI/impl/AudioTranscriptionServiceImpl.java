package tg.fitnessbot.services.AI.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tg.fitnessbot.api.VoskClient;
import tg.fitnessbot.services.AI.AudioTranscriptionService;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        AudioFormat format = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, // 16 bit signed
                16000, // 16 kHz
                16, // bits per sample
                1, // mono
                2, // frame size (bytes per sample * channels)
                16000, // frame rate
                false // little-endian
        );
        AudioInputStream ais = new AudioInputStream(
                new ByteArrayInputStream(data),
                format,
                data.length / format.getFrameSize()
        );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }
}
