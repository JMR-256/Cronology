package jmr256.com.github.cronology.parser.componentcalculation;

import jmr256.com.github.cronology.parser.exceptions.IllegalCronException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static jmr256.com.github.cronology.parser.errorenums.CronMessages.*;


public class CronComponentCalculator implements ComponentCalculator {
    private final List<Integer> allVals;

    private final Integer minVal;
    private final Integer maxVal;
    public CronComponentCalculator(Integer minVal, Integer maxVal) {
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.allVals = IntStream.rangeClosed(minVal, maxVal).boxed().toList();
    }

    @Override
    public List<Integer> calculateComponent(String component) {
        try{
            if(component.equals("*")){
                return allVals;
            } else if(component.contains(",")){
                return calculateSubList(component);
            } else if (component.contains("/")){
                return  calculateStepFunction(component);
            } else if (component.contains("-")){
                return calculateRange(component);
            } else {
                return List.of(verifySingleValue(component));
            }
        } catch (NumberFormatException numberFormatException){
            throw new IllegalCronException(INVALID_CHARACTER.buildMessage(component));
        }
    }

    private List<Integer> calculateStepFunction(String component){
        String[] segments = component.split("/");

        if(segments.length != 2 || Integer.parseInt(segments[1]) < 1){
            throw new IllegalCronException(INVALID_STEP_FUNCTION.buildMessage(component));
        }

        List<Integer> interval = determineIntervalForStepFunction(segments[0]);

        if(interval.isEmpty()){
            throw new IllegalCronException(INVALID_STEP_FUNCTION.buildMessage(component));
        }

        Integer min = interval.get(0);
        Integer max = interval.get(interval.size()-1);
        List<Integer> result = new ArrayList<>();

        for(int i = min; i <= max; i += verifySingleValue(segments[1])){
            result.add(i);
        }
        return result;
    }

    private List<Integer> determineIntervalForStepFunction(String component){
        List<Integer> interval = new ArrayList<>();
        if(component.equals("*")){
            interval = allVals;

        } else if(component.contains("-")){
            interval = calculateRange(component);
        }

        return interval;
    }

    private static List<Integer> calculateRange(String component){
        String[] segments = component.split("-");
        if(segments.length != 2){
            throw new IllegalCronException(INVALID_RANGE_FUNCTION.buildMessage(component));
        }
        int min = Integer.parseInt(segments[0]);
        int max = Integer.parseInt(segments[1]);

        return IntStream.rangeClosed(min, max).boxed().toList();
    }

    private List<Integer> calculateSubList(String component){
        String[] segments = component.split(",");
        List<Integer> result = new ArrayList<>();

        for(String segment : segments){
            //TODO optimise logic with single loop
            if (segment.contains("/")){
                result.addAll(calculateStepFunction(segment));
           } else if (segment.contains("*")){
                return allVals;
            } else if (segment.contains("-")) {
                result.addAll(calculateRange(segment));
           } else {
                result.add(verifySingleValue(segment));
            }
        }
        return result;
    }

    private Integer verifySingleValue(String component){
        Integer result = Integer.parseInt(component);
        if(result >= minVal && result <= maxVal){
           return result;
        }
        throw new IllegalCronException(INVALID_VALUE_RANGE.buildMessage(component));
    }
}
