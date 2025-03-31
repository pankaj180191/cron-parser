package org.example.cornparser.concurrency;

import org.example.cornparser.model.CronExpression;
import org.example.cornparser.parser.CronParser;
import org.example.cornparser.parser.DefaultCronParser;

import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles parsing of cron expressions concurrently using a fixed thread pool.
 */
public class ConcurrentCronParser {
    private static final int THREAD_POOL_SIZE = 10;  // Number of threads in the pool
    private static final Logger LOGGER = Logger.getLogger(ConcurrentCronParser.class.getName());

    private final ExecutorService executorService;
    private final CronParser parser;

    /**
     * Constructor initializes the thread pool and cron parser.
     */
    public ConcurrentCronParser() {
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        this.parser = new DefaultCronParser();
    }

    /**
     * Parses the given cron expression asynchronously.
     *
     * @param cronExpression the cron expression to parse
     * @return a Future containing the parsed CronExpression object, or null if parsing fails
     */
    public Future<CronExpression> parseCronExpression(String cronExpression) {
        return executorService.submit(() -> {
            try {
                return parser.parse(cronExpression);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error parsing cron expression: " + cronExpression, e);
                throw new ExecutionException("Failed to parse cron expression: " + cronExpression, e);
            }
        });
    }

    /**
     * Gracefully shuts down the executor service, ensuring all tasks complete execution.
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {  // Wait for active tasks to finish
                LOGGER.warning("Forcing shutdown as tasks did not terminate in time.");
                executorService.shutdownNow();  // Forcefully terminate remaining tasks
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Shutdown interrupted, forcing immediate shutdown.", e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
