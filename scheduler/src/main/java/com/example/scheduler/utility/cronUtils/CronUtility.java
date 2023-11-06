package com.example.scheduler.utility.cronUtils;
import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.definition.CronDefinition;
import static com.cronutils.model.field.expression.FieldExpressionFactory.*;
import com.cronutils.model.field.value.SpecialChar;

public class CronUtility {
    public static String buildDailyCronExpression(){
        return CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
                        .withYear(always())
                        .withDoM(between(SpecialChar.L, 3))
                        .withMonth(always())
                        .withDoW(questionMark())
                        .withHour(always())
                        .withMinute(always())
                        .withSecond(on(0))
                        .instance().asString();
    }
}
