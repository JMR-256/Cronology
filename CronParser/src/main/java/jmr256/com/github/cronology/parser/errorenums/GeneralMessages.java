package jmr256.com.github.cronology.parser.errorenums;

public enum GeneralMessages {
    INVALID_NUM_ARGS("Invalid arguments supplied please use format: \"* * * * * /usr/bin/someCommand\""),
    UNEXPECTED_ERR("An unexpected error has occurred: ");
    private final String message;
    public String getMessage() {
        return message;
    }
    GeneralMessages(String message) {
        this.message = message;
    }
}
