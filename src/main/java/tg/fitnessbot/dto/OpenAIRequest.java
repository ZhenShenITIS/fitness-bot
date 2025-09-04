package tg.fitnessbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record OpenAIRequest(
        String model,
        @JsonProperty("input")String prompt
) {
}
