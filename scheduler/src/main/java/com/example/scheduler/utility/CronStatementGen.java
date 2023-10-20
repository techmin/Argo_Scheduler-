package com.example.scheduler.utility;
import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.expression.FieldExpressionFactory;
import org.hibernate.grammars.hql.HqlParser;

import static com.cronutils.model.field.expression.FieldExpressionFactory.*;

public class CronStatementGen {

    public static void createCronExpFromInput(){

        System.out.println("If you would lik");
    }

    static Cron expression;
/*
    public static Cron cronExp(int Year, int DoM, int Month, int DoW, int Hour, int Minute, int second )
    {


        // expression = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ)).withHour(every(on(12),12)).instance();
        String cron = ;
         expression =
         return expression;
    }

*/
    public static Cron setSecond(Cron exp, int second )
    {
        //expression.withSecond(on(12));
        return null;
    }
}
