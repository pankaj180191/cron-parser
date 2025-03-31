package org.example.cornparser.parser;

import org.example.cornparser.model.CronExpression;

public interface CronParser {
    CronExpression parse(String cronExpression);
}
