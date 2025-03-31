package org.example.cornparser;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CronScheduler {

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
