package jmr256.com.github.cronology.parser;

import jmr256.com.github.cronology.parser.componentcalculation.*;
import jmr256.com.github.cronology.parser.exceptions.IllegalCronException;

import static jmr256.com.github.cronology.parser.errorenums.CronMessages.*;
import static jmr256.com.github.cronology.parser.errorenums.GeneralMessages.UNEXPECTED_ERR;

public class CronParser implements Parser {

    private final ComponentCalculator minsCalculator;
    private final ComponentCalculator hoursCalculator;
    private final ComponentCalculator daysOfMonthCalculator;
    private final ComponentCalculator monthsCalculator;
    private final ComponentCalculator daysOfWeekCalculator;

    public CronParser(ComponentCalculator minsCalculator, ComponentCalculator hoursCalculator, ComponentCalculator daysOfMonthCalculator, ComponentCalculator monthsCalculator, ComponentCalculator daysOfWeekCalculator) {
        this.minsCalculator = minsCalculator;
        this.hoursCalculator = hoursCalculator;
        this.daysOfMonthCalculator = daysOfMonthCalculator;
        this.monthsCalculator = monthsCalculator;
        this.daysOfWeekCalculator = daysOfWeekCalculator;
    }

    @Override
    public String parse(String expression) {
        String result;
        try {
            result = expressionToCron(expression).toString();
        } catch (Exception exception){
            if(exception instanceof IllegalCronException) {
                result = exception.getMessage();
            } else {
                System.err.println(UNEXPECTED_ERR.getMessage());
                throw exception;
            }
        }
        return result;
    }

    private Cron expressionToCron(String expression) throws IllegalArgumentException {
        String[] components = expression.split(" ");

        if (components.length < 5 || components.length > 6) {
            throw new IllegalCronException(INVALID_PARAM_FORMAT.buildMessage(expression));
        }

        String command = components.length == 6 ? components[5] : "None provided";

        return new Cron(minsCalculator.calculateComponent(components[0]),
                hoursCalculator.calculateComponent(components[1]),
                daysOfMonthCalculator.calculateComponent(components[2]),
                monthsCalculator.calculateComponent(components[3]),
                daysOfWeekCalculator.calculateComponent(components[4]),
                command);
    }

}
