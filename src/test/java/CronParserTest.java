import org.example.cornparser.model.CronExpression;
import org.example.cornparser.parser.DefaultCronParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class CronParserTest {

    private final DefaultCronParser parser = new DefaultCronParser();

    @Test
    public void testParseSimpleCron() {
        CronExpression expression = parser.parse("*/15 0 1,15 * 1-5 /usr/bin/find");

        assertEquals(List.of(0, 15, 30, 45), expression.getMinutes());
        assertEquals(List.of(0), expression.getHours());
        assertEquals(List.of(1, 15), expression.getDaysOfMonth());
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), expression.getMonths());
        assertEquals(List.of(1, 2, 3, 4, 5), expression.getDaysOfWeek());
        assertEquals("/usr/bin/find", expression.getCommand());
    }

    @Test
    public void testInvalidCronExpression() {
       assertThrows(IllegalArgumentException.class, () -> {
            parser.parse("*/15 0 * 1-5 /usr/bin/find");
        });
    }

}
