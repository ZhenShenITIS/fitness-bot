package tg.fitnessbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FitnessBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessBotApplication.class, args);
    }

}
