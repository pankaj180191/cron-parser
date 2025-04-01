package org.example.cornparser.parser;

import org.example.cornparser.model.CronExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static org.example.cornparser.validation.CronValidator.validateCronField;

public class DefaultCronParser implements CronParser {
    private static final int EXPECTED_FIELDS = 6; // A valid cron expression must have exactly 6 fields.


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
        if (parts.length < EXPECTED_FIELDS) {
            throw new IllegalArgumentException("Invalid cron expression: Expected 6 fields, found " + parts.length);
        }

        // Validate each cron field based on its allowed range.
        validateCronField(parts[0], 0, 59, "minute");
        validateCronField(parts[1], 0, 23, "hour");
        validateCronField(parts[2], 1, 31, "day of month");
        validateCronField(parts[3], 1, 12, "month");
        validateCronField(parts[4], 0, 7, "day of week");

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
        String[] commandParts = Arrays.copyOfRange(parts, 5, parts.length);
        String command = processCommand(commandParts);

        // Return a CronExpression object containing the parsed values.
        return new CronExpression(minutes, hours, daysOfMonth, months, daysOfWeek, command);
    }

    public static String processCommand(String[] command) {
        if (command == null || command.length == 0) {
            throw new IllegalArgumentException("Command cannot be null or empty");
        }

        String mainCommand = command[0];
        List<String> subCommands = new ArrayList<>();

        for (int i = 1; i < command.length; i += 1) {
            //String key = command[i].replace("-", "");
            //String value = command[i + 1];
            subCommands.add(command[i]);
        }

        return mainCommand + " " + String.join(" ", subCommands);
    }

}
