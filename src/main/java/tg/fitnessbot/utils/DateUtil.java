package tg.fitnessbot.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

public class DateUtil {
    public static int getAge (LocalDate dateOfBirth) {
        int bYear = dateOfBirth.getYear();
        int bMonth = dateOfBirth.getMonthValue();
        int bDay = dateOfBirth.getDayOfMonth();

        int cYear = LocalDate.now().getYear();
        int cMonth = LocalDate.now().getMonthValue();
        int cDay = LocalDate.now().getDayOfMonth();

        if (cMonth > bMonth) {
            return bYear - cYear;
        } else if (cMonth < bMonth) {
            return bYear - cYear - 1;
        } else {
            if (cDay < bDay) {
                return bYear - cYear - 1;
            } else {
                return bYear - cYear;
            }
        }
    }
}
