package org.example.cornparser.parser;

import org.example.cornparser.model.CronExpression;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import static org.example.cornparser.validation.CronValidator.validateCronField;

public class DefaultCronParser implements CronParser {
    private static final int EXPECTED_FIELDS = 6; // A valid cron expression must have exactly 6 fields.
    private static final int MIN_FIELDS = 6; // Traditional cron has 6 fields
    private static final int MAX_FIELDS = 7; // With optional year field


    /**
     * Parses a cron expression string and returns a CronExpression object.
     *
     * @param cronExpression The input cron string.
     * @return A CronExpression object containing parsed values.
     * @throws IllegalArgumentException if the cron expression is invalid.
     */
    @Override
    public CronExpression parse(String cronExpression) {
        // Split the cron expression into its individual fields.
        String[] parts = cronExpression.trim().split("\\s+");

        // Validate that the cron expression has the expected number of fields.
        if (parts.length < MIN_FIELDS || parts.length > MAX_FIELDS) {
            throw new IllegalArgumentException("Invalid cron expression: Expected 6 fields, found " + parts.length);
        }

        // Validate each cron field based on its allowed range.
        validateCronField(parts[0], 0, 59, "minute");
        validateCronField(parts[1], 0, 23, "hour");
        validateCronField(parts[2], 1, 31, "day of month");
        validateCronField(parts[3], 1, 12, "month");
        validateCronField(parts[4], 0, 7, "day of week");

        Optional<List<Integer>> years = Optional.empty();
        if (parts.length == 7) {
            validateCronField(parts[5], 1970, 2099, "year");
            years = Optional.of(new CronFieldParser(1970, 2099).parse(parts[5]));
        }

        // Create field parsers for each cron field.
        CronFieldParser minuteParser = new CronFieldParser(0, 59);
        CronFieldParser hourParser = new CronFieldParser(0, 23);
        CronFieldParser dayOfMonthParser = new CronFieldParser(1, 31);
        CronFieldParser monthParser = new CronFieldParser(1, 12);
        CronFieldParser dayOfWeekParser = new CronFieldParser(0, 6);

        // Parse the individual fields into lists of integers.
        List<Integer> minutes = minuteParser.parse(parts[0]);
        List<Integer> hours = hourParser.parse(parts[1]);
        List<Integer> daysOfMonth = dayOfMonthParser.parse(parts[2]);
        List<Integer> months = monthParser.parse(parts[3]);
        List<Integer> daysOfWeek = dayOfWeekParser.parse(parts[4]);

        // Extract the command to be executed.
        String command = parts[parts.length - 1];

        // Return a CronExpression object containing the parsed values.
        return new CronExpression(minutes, hours, daysOfMonth, months, daysOfWeek, years, command);
    }
}
