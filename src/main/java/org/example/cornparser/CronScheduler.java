package org.example.cornparser;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CronScheduler {

    /*
    Corner Cases
    1️⃣ What if day of week conflicts with day of month?
    Example:

    "day of month": [15]

    "day of week": [0] (Sunday)

    If 15th of a month is not a Sunday, the job won't run that month.

    2️⃣ What if day of month = * and day of week = *?
    The job will run every minute of every day.

    3️⃣ Leap Year Handling (Feb 29)
    Example: "day of month": [29], "month": [2]

    The job will only execute in leap years (e.g., 2024, 2028).

    4️⃣ Invalid Dates
    Example: "day of month": [31], "month": [4]

    April has only 30 days, so this should be skipped.

    5️⃣ Current Time Greater Than Next Possible Time
    Example:

    "minute": [0], "hour": [9]

    Current time = March 28, 2025, 10:30 AM

    Next valid execution → April 1st at 9:00 AM, not today.

    6️⃣ Time Rolls Over to Next Year
    Example:

    "month": [12], "day of month": [31]

    Current time = Dec 30, 2025, 11:00 PM

    Next valid execution → Dec 31, 2025, 11:00 PM

    If hour = 23, minute = 59, next execution may be next year.
     */
    public static List<String> findNextNOccurrences(Map<String, List<Integer>> parsedCron, int n) {
        LocalDateTime currentTime = LocalDateTime.now();
        int currentYear = currentTime.getYear();
        List<String> nextOccurrences = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        while (nextOccurrences.size() < n) {
            for (int month : parsedCron.get("month")) {
                if (currentTime.getYear() == currentYear && currentTime.getMonthValue() > month) {
                    continue;
                }
                for (int dayOfMonth : parsedCron.get("day of month")) {
                    for (int hour : parsedCron.get("hour")) {
                        for (int minute : parsedCron.get("minute")) {
                            try {
                                LocalDateTime nextTime = LocalDateTime.of(currentYear, month, dayOfMonth, hour, minute);
                                if (!currentTime.isAfter(nextTime) && parsedCron.get("day of week").contains(nextTime.getDayOfWeek().getValue())) {
                                    nextOccurrences.add(nextTime.format(formatter));
                                }
                            } catch (DateTimeException ignored) {
                                // Ignore invalid dates (like Feb 30)
                            }
                            if (nextOccurrences.size() >= n) {
                                return nextOccurrences;
                            }
                        }
                    }
                }
            }
            currentYear++;
        }
        return nextOccurrences;
    }
}
