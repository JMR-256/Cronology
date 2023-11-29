package jmr256.com.github.cronology.parser;

import java.util.List;
import java.util.StringJoiner;

public class Cron {
    private final List<Integer> minutes;
    private final List<Integer> hours;
    private final List<Integer> daysOfMonth;
    private final List<Integer> months;
    private final List<Integer> daysOfWeek;
    private final String command;

    public Cron(List<Integer> minutes, List<Integer> hours, List<Integer> daysOfMonth, List<Integer> months, List<Integer> daysOfWeek, String command) {
        this.minutes = minutes;
        this.hours = hours;
        this.daysOfMonth = daysOfMonth;
        this.months = months;
        this.daysOfWeek = daysOfWeek;
        this.command = command;
    }

    enum CronNamesWithSpacing {
        MINS("minutes       "),
        HOURS("hours         "),
        DAYS_OF_MONTH("days of month "),
        MONTHS("months        "),
        DAYS_OF_WEEK("days of week  "),
        COMMAND("command       ");

        public String getComponent() {
            return component;
        }

        private final String component;

        CronNamesWithSpacing(String component) {
            this.component = component;
        }
    }

    @Override
    public String toString(){
        return CronNamesWithSpacing.MINS.getComponent() + listFormatter(minutes) + "\n" +
                CronNamesWithSpacing.HOURS.getComponent() + listFormatter(hours) + "\n" +
                CronNamesWithSpacing.DAYS_OF_MONTH.getComponent() + listFormatter(daysOfMonth) + "\n" +
                CronNamesWithSpacing.MONTHS.getComponent() + listFormatter(months) + "\n" +
                CronNamesWithSpacing.DAYS_OF_WEEK.getComponent() + listFormatter(daysOfWeek) + "\n" +
                CronNamesWithSpacing.COMMAND.getComponent() + command + "\n";

    }

    private String listFormatter(List<Integer> list){
        StringJoiner stringJoiner = new StringJoiner(" ");
        for(Integer val : list){
            stringJoiner.add(String.valueOf(val));
        }
        return stringJoiner.toString();
    }
}
