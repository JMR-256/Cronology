package jmr256.com.github.cronology.parser.errorenums;

public enum CronMessages {
    INVALID_PARAM_FORMAT("Invalid parameter format: %s"),
    INVALID_CHARACTER("Invalid Cron provided, couldn't parse character: %s"),
    INVALID_STEP_FUNCTION("Invalid Cron provided, unable to calculate step function: %s"),
    INVALID_RANGE_FUNCTION("Invalid Cron provided, unable to calculate range function: %s"),
    INVALID_VALUE_RANGE("Invalid Cron provided, value outside of range: %s");
    private final String message;
    CronMessages(String message) {
        this.message = message;
    }

    public String buildMessage(String component){
        return String.format(message, component);
    }
}
