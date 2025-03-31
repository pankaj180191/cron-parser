package org.example.cornparser.parser;

import java.util.ArrayList;
import java.util.List;

public class CronFieldParser {
    private final int minValue;
    private final int maxValue;

    /**
     * Constructor to initialize min and max values for the cron field.
     * @param minValue Minimum allowed value for the field.
     * @param maxValue Maximum allowed value for the field.
     */
    public CronFieldParser(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Parses a cron field string and returns a list of valid integers.
     * Supports: Wildcard (*), Step (/), Range (-), List (,), and Single values.
     *
     * @param field The cron field string.
     * @return List of integers representing the parsed values.
     * @throws IllegalArgumentException if the input format is invalid.
     */
    public List<Integer> parse(String field) {
        List<Integer> values = new ArrayList<>();

        try {
            if (field.equals("*")) {  // Wildcard case (e.g., "*")
                for (int i = minValue; i <= maxValue; i++) {
                    values.add(i);
                }
            } else if (field.contains("/")) {  // Step case (e.g., "*/15" or "5/10")
                String[] parts = field.split("/");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid step format: " + field);
                }

                int step = Integer.parseInt(parts[1]);
                if (step <= 0) {
                    throw new IllegalArgumentException("Step value must be positive: " + step);
                }

                int start = parts[0].equals("*") ? minValue : Integer.parseInt(parts[0]);
                if (start < minValue || start > maxValue) {
                    throw new IllegalArgumentException("Start value out of range: " + start);
                }

                for (int i = start; i <= maxValue; i += step) {
                    values.add(i);
                }
            } else if (field.contains("-")) {  // Range case (e.g., "1-5")
                String[] parts = field.split("-");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid range format: " + field);
                }

                int start = Integer.parseInt(parts[0]);
                int end = Integer.parseInt(parts[1]);

                if (start > end || start < minValue || end > maxValue) {
                    throw new IllegalArgumentException("Invalid range: " + start + "-" + end);
                }

                for (int i = start; i <= end; i++) {
                    values.add(i);
                }
            } else if (field.contains(",")) {  // List case (e.g., "1,3,5")
                String[] parts = field.split(",");
                for (String part : parts) {
                    int value = Integer.parseInt(part.trim());
                    if (value < minValue || value > maxValue) {
                        throw new IllegalArgumentException("Value out of range: " + value);
                    }
                    values.add(value);
                }
            } else {  // Single number case (e.g., "5")
                int value = Integer.parseInt(field);
                if (value < minValue || value > maxValue) {
                    throw new IllegalArgumentException("Value out of range: " + value);
                }
                values.add(value);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in field: " + field, e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid cron field structure: " + field, e);
        }

        return values;
    }
}
