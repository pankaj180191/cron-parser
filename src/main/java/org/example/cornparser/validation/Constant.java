package org.example.cornparser.validation;

import java.util.HashMap;
import java.util.Map;

public class Constant {

    public static final Map<String, Integer> DAY_NAME_TO_NUMBER = new HashMap<>();
    public static final Map<Integer, String> NUMBER_TO_DAY_NAME = new HashMap<>();
    public static final Map<String, Integer> MONTH_NAME_TO_NUMBER = new HashMap<>();
    public static final Map<Integer, String> NUMBER_TO_MONTH_NAME = new HashMap<>();

    static {
        // Day mapping (1 = Sunday, 7 = Saturday)
        DAY_NAME_TO_NUMBER.put("SUN", 1);
        DAY_NAME_TO_NUMBER.put("MON", 2);
        DAY_NAME_TO_NUMBER.put("TUE", 3);
        DAY_NAME_TO_NUMBER.put("WED", 4);
        DAY_NAME_TO_NUMBER.put("THU", 5);
        DAY_NAME_TO_NUMBER.put("FRI", 6);
        DAY_NAME_TO_NUMBER.put("SAT", 7);

        for (Map.Entry<String, Integer> entry : DAY_NAME_TO_NUMBER.entrySet()) {
            NUMBER_TO_DAY_NAME.put(entry.getValue(), entry.getKey());
        }

        // Month mapping
        MONTH_NAME_TO_NUMBER.put("JAN", 1);
        MONTH_NAME_TO_NUMBER.put("FEB", 2);
        MONTH_NAME_TO_NUMBER.put("MAR", 3);
        MONTH_NAME_TO_NUMBER.put("APR", 4);
        MONTH_NAME_TO_NUMBER.put("MAY", 5);
        MONTH_NAME_TO_NUMBER.put("JUN", 6);
        MONTH_NAME_TO_NUMBER.put("JUL", 7);
        MONTH_NAME_TO_NUMBER.put("AUG", 8);
        MONTH_NAME_TO_NUMBER.put("SEP", 9);
        MONTH_NAME_TO_NUMBER.put("OCT", 10);
        MONTH_NAME_TO_NUMBER.put("NOV", 11);
        MONTH_NAME_TO_NUMBER.put("DEC", 12);

        for (Map.Entry<String, Integer> entry : MONTH_NAME_TO_NUMBER.entrySet()) {
            NUMBER_TO_MONTH_NAME.put(entry.getValue(), entry.getKey());
        }
    }
}
