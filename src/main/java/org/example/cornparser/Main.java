package org.example.cornparser;

import org.example.cornparser.concurrency.ConcurrentCronParser;
import org.example.cornparser.model.CronExpression;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //Handle Concurrent Request
        ConcurrentCronParser concurrentParser = new ConcurrentCronParser();

        Future<CronExpression> future1 = concurrentParser.parseCronExpression("* 10-4 * * * /usr/bin/find abc xyz");
        Future<CronExpression> future2 = concurrentParser.parseCronExpression("12 * * 1 * /usr/bin/backup");

        // Retrieve results
        CronExpression result1 = future1.get();
        CronExpression result2 = future2.get();

        result1.printCronExpression();
        result2.printCronExpression();

        concurrentParser.shutdown();
    }
}