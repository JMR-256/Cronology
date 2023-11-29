package jmr256.com.github.cronology;

import jmr256.com.github.cronology.parser.CronParser;
import jmr256.com.github.cronology.parser.Parser;
import jmr256.com.github.cronology.parser.componentcalculation.CronComponentCalculator;

import static jmr256.com.github.cronology.parser.errorenums.GeneralMessages.*;

public class CronParserApplication {
    private static final Parser parser = new CronParser(
            new CronComponentCalculator(0, 59),
            new CronComponentCalculator(0, 23),
            new CronComponentCalculator(1, 31),
            new CronComponentCalculator(1, 12),
            new CronComponentCalculator(0, 6)
    );

    public static void main(String[] args) {
       if(!isValidArgs(args)){
           System.err.println(INVALID_NUM_ARGS.getMessage());
       } else {
           System.out.println(parser.parse(args[0]));
       }
    }

    private static boolean isValidArgs(String[] args){
        return args.length == 1;
    }
}
