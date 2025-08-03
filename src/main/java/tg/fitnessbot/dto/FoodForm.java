package tg.fitnessbot.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodForm {
    private String name;
    private double kcal;
    private double protein;
    private double fat;
    private double carbohydrates;
}
