package org.example.cornparser.model;

import lombok.Data;

import java.util.List;

/**
 * Represents a parsed cron expression with individual time fields and command.
 */
@Data
public class CronExpression {
    private final List<Integer> minutes;
    private final List<Integer> hours;
    private final List<Integer> daysOfMonth;
    private final List<Object> months;
    private final List<Object> daysOfWeek;
    private final String command;

    /**
     * Constructs a CronExpression with parsed values.
     *
     * @param minutes     List of minutes parsed from cron expression.
     * @param hours       List of hours parsed from cron expression.
     * @param daysOfMonth List of days of the month parsed from cron expression.
     * @param months      List of months parsed from cron expression.
     * @param daysOfWeek  List of days of the week parsed from cron expression.
     * @param command     The command to be executed.
     * @throws IllegalArgumentException if any list is null or empty, or if command is invalid.
     */
    public CronExpression(List<Integer> minutes, List<Integer> hours, List<Integer> daysOfMonth,
                          List<Object> months, List<Object> daysOfWeek, String command) {
        this.minutes = minutes;
        this.hours = hours;
        this.daysOfMonth = daysOfMonth;
        this.months = months;
        this.daysOfWeek = daysOfWeek;
        this.command = command.trim(); // Trim to remove leading/trailing spaces
    }

    /**
     * Prints the parsed cron expression in a readable format.
     */
    public void printCronExpression() {
        System.out.printf("%-14s%s%n", "minute", formatList(minutes));
        System.out.printf("%-14s%s%n", "hour", formatList(hours));
        System.out.printf("%-14s%s%n", "day of month", formatList(daysOfMonth));
        System.out.printf("%-14s%s%n", "month", formatListObj(months));
        System.out.printf("%-14s%s%n", "day of week", formatListObj(daysOfWeek));
        System.out.printf("%-14s%s%n", "command", command);
        System.out.println("****************************");
    }

    /**
     * Formats a list of integers into a string, removing brackets and commas.
     *
     * @param list List of integers.
     * @return Formatted string representation of the list.
     */
    private String formatList(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return "N/A";  // Return "N/A" for empty or null lists
        }
        return list.toString().replaceAll("[\\[\\],]", "");
    }

    private String formatListObj(List<Object> list) {
        if (list == null || list.isEmpty()) {
            return "N/A";  // Return "N/A" for empty or null lists
        }
        return list.toString().replaceAll("[\\[\\],]", "");
    }
}
