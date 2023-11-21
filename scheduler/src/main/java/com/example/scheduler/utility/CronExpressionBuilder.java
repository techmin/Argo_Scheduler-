package com.example.scheduler.utility;

public class CronExpressionBuilder {
    public static String buildCronExpression(final String seconds, final String minutes, 
                                            final String hours, final String dayOfMonth, 
                                            final String month, final String dayOfWeek, 
                                            final String year)
    {
        return String.format("%s %s %s %s %s %s", seconds, minutes, hours, dayOfMonth, month, dayOfWeek, year);
    }
}
