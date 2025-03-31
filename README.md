# Cron Parser

## Overview
This project provides a concurrent cron expression parser that can handle multiple cron expression validation and parsing requests simultaneously. It validates cron fields, supports special cron keywords, and extracts schedule details efficiently.

## Features
- **Concurrent Parsing:** Uses a thread pool to handle multiple cron parsing requests.
- **Validation of Cron Fields:** Ensures cron expressions adhere to valid formats.
- **Efficient Parsing:** Supports wildcards (`*`), ranges (`1-5`), steps (`*/15`), and lists (`1,5,10`).

## Usage
The `Main` class demonstrates how to use `ConcurrentCronParser` to parse cron expressions concurrently. Example:
```java
ConcurrentCronParser concurrentParser = new ConcurrentCronParser();
Future<CronExpression> future = concurrentParser.parseCronExpression("*/15 0 1,15 * 1-5 /usr/bin/find");
CronExpression result = future.get();
result.printCronExpression();
concurrentParser.shutdown();
```

## Supported Cron Syntax
| Syntax    | Example   | Description |
|-----------|-----------|-------------|
| Wildcard  | `*`       | Matches all values |
| Range     | `1-5`     | Matches values between 1 and 5 |
| Step      | `*/15`    | Matches every 15th value |
| List      | `1,5,10`  | Matches listed values |

## How It Works
1. **Validation:** `CronValidator` checks the cron format.
2. **Parsing:** `DefaultCronParser` splits and processes each field.
3. **Execution:** `ConcurrentCronParser` runs parsing tasks asynchronously.

## Example Output
```
Minutes: [0, 15, 30, 45]
Hours: [0]
Days of Month: [1, 15]
Months: [*]
Days of Week: [1, 2, 3, 4, 5]
Command: /usr/bin/find
```
