package org.example.cornparser.model;

import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * Represents a parsed cron expression with individual time fields and command.
 */
@Data
public class CronExpression {
    private final List<Integer> minutes;
    private final List<Integer> hours;
    private final List<Integer> daysOfMonth;
    private final List<Integer> months;
    private final List<Integer> daysOfWeek;
    private final Optional<List<Integer>> years;
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
                          List<Integer> months, List<Integer> daysOfWeek, Optional<List<Integer>> years, String command) {
        this.minutes = minutes;
        this.hours = hours;
        this.daysOfMonth = daysOfMonth;
        this.months = months;
        this.daysOfWeek = daysOfWeek;
        this.years = years;
        this.command = command.trim(); // Trim to remove leading/trailing spaces
    }

    /**
     * Prints the parsed cron expression in a readable format.
     */
    public void printCronExpression() {
        System.out.printf("%-14s%s%n", "minute", formatList(minutes));
        System.out.printf("%-14s%s%n", "hour", formatList(hours));
        System.out.printf("%-14s%s%n", "day of month", formatList(daysOfMonth));
        System.out.printf("%-14s%s%n", "month", formatList(months));
        System.out.printf("%-14s%s%n", "day of week", formatList(daysOfWeek));
        if (years.isPresent()) {
            System.out.printf("%-14s%s%n", "year", formatList(years.get()));
        }
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
}
