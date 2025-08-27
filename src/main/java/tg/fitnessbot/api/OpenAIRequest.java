package tg.fitnessbot.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


public record OpenAIRequest(
        String model,
        @JsonProperty("input")String prompt
) {
}
