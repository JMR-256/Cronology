package jmr256.com.github.cronology.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

class CronTest {

    private Cron cron = new Cron(
            List.of(5,6,7),
            List.of(11,12),
            List.of(1,2,3,4,5),
            List.of(11,12),
            List.of(0 ,1, 2, 3, 4, 5, 6),
            "example");

    @Test
    void cronToStringFormatsCronValues(){
        String expectedResult =
                "minutes       5 6 7\n" +
                "hours         11 12\n" +
                "days of month 1 2 3 4 5\n" +
                "months        11 12\n" +
                "days of week  0 1 2 3 4 5 6\n" +
                "command       example\n";
        Assertions.assertEquals(expectedResult, cron.toString());
    }

    @Test
    void cronToStringNotInvalidSpacing(){
        String expectedResult =
                " minutes       5 6 7\n" +
                        "hours         11 12\n" +
                        "days of month 1 2 3 4 5\n" +
                        "months        11 12\n" +
                        "days of week  0 1 2 3 4 5 6\n" +
                        "command       example\n";
        Assertions.assertNotEquals(expectedResult, cron.toString());
    }

}