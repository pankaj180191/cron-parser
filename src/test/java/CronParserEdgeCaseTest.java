import org.example.cornparser.parser.DefaultCronParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CronParserEdgeCaseTest {
    private final DefaultCronParser parser = new DefaultCronParser();

    @Test
    public void testInvalidMinuteField() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("-1 0 1 * * /usr/bin/find"));
    }

    @Test
    public void testInvalidHourField() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("0 24 1 * * /usr/bin/find"));
    }

    @Test
    public void testInvalidDayOfMonth() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("0 0 32 * * /usr/bin/find"));
    }

    @Test
    public void testInvalidMonthField() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("0 0 1 13 * /usr/bin/find"));
    }

    @Test
    public void testInvalidDayOfWeek() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("0 0 1 * 7 /usr/bin/find"));
    }
}
