package org.example.cornparser;

import org.example.cornparser.concurrency.ConcurrentCronParser;
import org.example.cornparser.model.CronExpression;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.example.cornparser.CronScheduler.findNextNOccurrences;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //Handle Concurrent Request
//        ConcurrentCronParser concurrentParser = new ConcurrentCronParser();
//
//        Future<CronExpression> future1 = concurrentParser.parseCronExpression("* 1-10 * * * /usr/bin/find");
//        Future<CronExpression> future2 = concurrentParser.parseCronExpression("12 * * 1 * /usr/bin/backup");
//
//        // Retrieve results
//        CronExpression result1 = future1.get();
//        CronExpression result2 = future2.get();
//
//        result1.printCronExpression();
//        result2.printCronExpression();
//
//        concurrentParser.shutdown();


        Map<String, List<Integer>> parsedCron = new HashMap<>();
        parsedCron.put("minute", Arrays.asList(0));
        parsedCron.put("hour", Arrays.asList(9, 15));
        parsedCron.put("day of month", Arrays.asList(1, 15));
        parsedCron.put("month", Arrays.asList(1, 7));
        parsedCron.put("day of week", Arrays.asList(1, 3, 5)); // Monday, Wednesday, Friday

        int n = 5;
        List<String> occurrences = findNextNOccurrences(parsedCron, n);

        System.out.println("Next " + n + " occurrences:");
        for (String occurrence : occurrences) {
            System.out.println(occurrence);
        }
    }
}