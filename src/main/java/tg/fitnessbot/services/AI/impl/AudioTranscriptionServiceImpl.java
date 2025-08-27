package tg.fitnessbot.services.AI.impl;


import org.json.JSONObject;
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
import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AudioTranscriptionServiceImpl implements AudioTranscriptionService {
    @Autowired
    VoskClient voskClient;

    @Override
    public String transcribeAudio(File audio) {
        byte[] wavBytes = convert(audio);
        ResponseEntity<String> response = voskClient.getResponse(wavBytes);
        String text;
        JSONObject json = new JSONObject(response.getBody());
        text = json.getString("text");


        Set<String> hexItems = new HashSet<>();

        Matcher m = Pattern.compile("\\\\u[a-fA-f0-9]{4}").matcher(text);
        while (m.find()) {
            hexItems.add(m.group());
        }

        for (String unicodeHex : hexItems) {
            int hexVal = Integer.parseInt(unicodeHex.substring(2), 16);
            text = text.replace(unicodeHex, "" + ((char) hexVal));
        }
        return text;
    }

    // TODO Оптимизировать данный метод
    private byte[] convert(File source) {
        MultimediaObject sourceFile = new MultimediaObject(source);
        File target = null;
        try {
            target = File.createTempFile("convert", "wav");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("pcm_s16le");
        audio.setBitRate(16000);
        audio.setChannels(1);
        audio.setSamplingRate(16000);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("wav");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(sourceFile, target, attrs);
        } catch (EncoderException e) {
            throw new RuntimeException(e);
        }

        // В этом месте происходит загрузка оперативки
        byte[] wavBytes;
        try (FileInputStream fis = new FileInputStream(target)){
            wavBytes = fis.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        source.delete();
        target.delete();
        return wavBytes;

    }
}
