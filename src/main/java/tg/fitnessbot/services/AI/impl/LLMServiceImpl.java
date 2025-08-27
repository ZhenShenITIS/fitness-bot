package tg.fitnessbot.services.AI.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tg.fitnessbot.api.OpenAIClient;
import tg.fitnessbot.services.AI.LLMService;
@Service
public class LLMServiceImpl implements LLMService {
    @Autowired
    OpenAIClient openAIClient;

    @Override
    public String processAudio(String audio) {
        ResponseEntity<String> response = openAIClient.getResponse(audio);
        JSONObject json = new JSONObject(response.getBody());
        //JSONObject json2 = json.getJSONObject("output");
        String text = json.getString("output");
        return text;
    }
}
