package org.example.cornparser.validation;

import java.util.HashMap;
import java.util.Map;

public class CronValidator {

    private static final Map<String, Integer> DAY_NAME_TO_NUMBER = new HashMap<>();

    static {
        // Mapping day names to numeric values (MON-SUN → 1-7, following Unix cron standard)
        //DAY_NAME_TO_NUMBER.put("SUN", 0); // Optional: Some crons use 0 for Sunday
        DAY_NAME_TO_NUMBER.put("MON", 1);
        DAY_NAME_TO_NUMBER.put("TUE", 2);
        DAY_NAME_TO_NUMBER.put("WED", 3);
        DAY_NAME_TO_NUMBER.put("THU", 4);
        DAY_NAME_TO_NUMBER.put("FRI", 5);
        DAY_NAME_TO_NUMBER.put("SAT", 6);
        DAY_NAME_TO_NUMBER.put("SUN", 7); // Both 0 and 7 can represent Sunday
    }

    /**
     * Validates a cron field to ensure it follows the expected format.
     * It supports:
     * - Wildcard (*)
     * - Single numbers (e.g., "5")
     * - Ranges (e.g., "10-20")
     */
    public static void validateCronField(String field, int min, int max, String fieldName) {
        if (field.equals("*")) {
            return; // Wildcard is valid and means "any value", no need to validate further.
        }

        // Split the field by commas, as cron expressions can contain multiple values (e.g., "5,10,15")
        String[] parts = field.split(",");

        for (String part : parts) {
            if (part.contains("/")) {
                // Handle step values (e.g., "*/5", "10-30/5")
                String[] stepParts = part.split("/");

                // Ensure that the step format is correct (it must have exactly two parts)
                if (stepParts.length != 2) {
                    throw new IllegalArgumentException("Invalid step format in " + fieldName + ": " + field);
                }

                String rangeOrWildcard = stepParts[0]; // Can be a range (e.g., "10-30") or wildcard "*"
                String stepValue = stepParts[1]; // The step interval (e.g., "5")

                if (rangeOrWildcard.equals("*")) {
                    // If the format is "*/N", it means stepping through the full range
                    validateNumber(stepValue, 1, max - min, fieldName + " step");
                } else {
                    // If it's a range (e.g., "10-30/5"), validate the range first
                    validateRangeOrNumber(rangeOrWildcard, min, max, fieldName);
                    validateNumber(stepValue, 1, max - min, fieldName + " step");
                }
            } else if (part.contains("-")) {
                // Handle range values (e.g., "10-20")
                validateRangeOrNumber(part, min, max, fieldName);
            } else {
                // Handle single numeric values (e.g., "5")
                validateNumber(part, min, max, fieldName);
            }
        }
    }


    /**
     * Validates if a given string value represents a valid number within a specified range.
     *
     * @param value     The string representation of the number.
     * @param min       The minimum allowed value.
     * @param max       The maximum allowed value.
     * @param fieldName The name of the field being validated (for error messages).
     * @throws IllegalArgumentException if the value is not a number or is out of range.
     */
    private static void validateNumber(String value, int min, int max, String fieldName) {
        try {
            int num = Integer.parseInt(value); // Convert the string to an integer
            if (num < min || num > max) { // Check if it's within the valid range
                throw new IllegalArgumentException(fieldName + " out of range: " + num);
            }
        } catch (NumberFormatException e) { // Handle non-numeric values
            throw new IllegalArgumentException("Invalid " + fieldName + " value: " + value);
        }
    }

    /**
     * Validates if a given string is either a single number or a valid range (e.g., "5-10").
     * Ensures both numbers in the range are within the allowed limits.
     *
     * @param range     The string containing either a single number or a range.
     * @param min       The minimum allowed value.
     * @param max       The maximum allowed value.
     * @param fieldName The name of the field being validated (for error messages).
     * @throws IllegalArgumentException if the format is incorrect, values are out of range, or start > end.
     */
    private static void validateRangeOrNumber(String range, int min, int max, String fieldName) {
        if (range.contains("-")) { // Check if the input represents a range
            String[] bounds = range.split("-");

            // Ensure the range has exactly two numbers
            if (bounds.length != 2) {
                throw new IllegalArgumentException("Invalid range format in " + fieldName + ": " + range);
            }

            // Validate both start and end of the range
            validateNumber(bounds[0], min, max, fieldName + " range start");
            validateNumber(bounds[1], min, max, fieldName + " range end");

            // Ensure that the range is valid (start ≤ end)
            int start = Integer.parseInt(bounds[0]);
            int end = Integer.parseInt(bounds[1]);
            if (start > end) {
                throw new IllegalArgumentException(fieldName + " range start must be ≤ end: " + range);
            }
        } else { // If it's not a range, validate it as a single number
            validateNumber(range, min, max, fieldName);
        }
    }

    public static void validateCronFieldV2(String field, int min, int max, String fieldName) {
        if (field.equals("*")) {
            return; // Wildcard is valid
        }

        String[] parts = field.split(",");

        for (String part : parts) {
            if (part.contains("/")) {
                // Handle step values (e.g., "*/5", "10-30/5", "MON-THU/2")
                String[] stepParts = part.split("/");
                if (stepParts.length != 2) {
                    throw new IllegalArgumentException("Invalid step format in " + fieldName + ": " + field);
                }

                String rangeOrWildcard = stepParts[0];
                String stepValue = stepParts[1];

                if (rangeOrWildcard.equals("*")) {
                    validateNumber(stepValue, 1, max - min, fieldName + " step");
                } else {
                    validateRangeOrDayNames(rangeOrWildcard, fieldName);
                    validateNumber(stepValue, 1, max - min, fieldName + " step");
                }
            } else if (part.contains("-")) {
                // Handle ranges like "10-20" or "MON-THU"
                validateRangeOrDayNames(part, fieldName);
            } else {
                // Handle single values like "5" or "MON"
                validateSingleValueOrDayName(part, fieldName);
            }
        }
    }

    private static void validateSingleValueOrDayName(String value, String fieldName) {
        if (DAY_NAME_TO_NUMBER.containsKey(value)) {
            return; // Valid named day (e.g., MON, TUE)
        }
        validateNumber(value, 0, 7, fieldName); // Allow numeric days (0-7)
    }

    private static void validateRangeOrDayNames(String range, String fieldName) {
        String[] bounds = range.split("-");
        if (bounds.length != 2) {
            throw new IllegalArgumentException("Invalid range format in " + fieldName + ": " + range);
        }

        // Convert day names to numbers if needed
        int start = DAY_NAME_TO_NUMBER.getOrDefault(bounds[0], -1);
        int end = DAY_NAME_TO_NUMBER.getOrDefault(bounds[1], -1);

        if (start == -1) start = Integer.parseInt(bounds[0]);
        if (end == -1) end = Integer.parseInt(bounds[1]);

        if (start < 0 || start > 7 || end < 0 || end > 7) {
            throw new IllegalArgumentException(fieldName + " out of range: " + range);
        }

        if (start > end) {
            throw new IllegalArgumentException(fieldName + " range start must be ≤ end: " + range);
        }
    }

}
