package jmr256.com.github.cronology.parser.componentcalculation;

import jmr256.com.github.cronology.parser.exceptions.IllegalCronException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;


class CronComponentCalculatorTest {

    @ParameterizedTest
    @MethodSource("rangeProvider")
    @DisplayName("WHEN a component is only an asterisk THEN return whole range")
    void calculateComponentShouldReturnAllValuesForAsterisk(int min, int max){
        CronComponentCalculator cronComponentCalculator = new CronComponentCalculator(min, max);
        Assertions.assertEquals(IntStream.rangeClosed(min, max).boxed().toList(), cronComponentCalculator.calculateComponent("*"));
    }

    private static Stream<Arguments> rangeProvider(){
        return Stream.of(
                Arguments.of(0, 59),
                Arguments.of(0, 23),
                Arguments.of(1, 31),
                Arguments.of(1, 12),
                Arguments.of(0, 6)
        );
    }

    @ParameterizedTest
    @MethodSource("stepFunctionRangeProvider")
    @DisplayName("WHEN a component is a step function over a range THEN return the staggered range")
    void stepFunctionReturnsCorrectSeriesForSomeRange(List<Integer> expectedResult, String component){
        CronComponentCalculator cronComponentCalculator = new CronComponentCalculator(0, 59);
        Assertions.assertEquals(expectedResult, cronComponentCalculator.calculateComponent(component));
    }

    private static Stream<Arguments> stepFunctionRangeProvider(){
        return Stream.of(
                Arguments.of(IntStream.rangeClosed(0, 59).boxed().toList(), "*/1"),
                Arguments.of(IntStream.rangeClosed(0, 59).boxed().filter(val -> val % 2 == 0).toList(), "*/2"),
                Arguments.of(List.of(0, 15, 30, 45), "*/15"),
                Arguments.of(List.of(10, 11, 12, 13, 14, 15), "10-15/1"),
                Arguments.of(List.of(10, 15), "10-15/5"),
                Arguments.of(List.of(10, 13), "10-15/3"),
                Arguments.of(List.of(10, 14, 18), "10-20/4")
                );
    }

    @ParameterizedTest
    @MethodSource("invalidStepFunctionProvider")
    @DisplayName("WHEN a component is a step function with more than two parts THEN an exception is thrown")
    void stepFunctionWithMoreThanTwoComponentsErrors(String invalidComponent){
        CronComponentCalculator cronComponentCalculator = new CronComponentCalculator(0, 59);
        Exception e = Assertions.assertThrows(IllegalCronException.class, () -> cronComponentCalculator.calculateComponent(invalidComponent));
        Assertions.assertEquals("Invalid Cron provided, unable to calculate step function: " + invalidComponent, e.getMessage());
    }

    private static Stream<Arguments> invalidStepFunctionProvider(){
        return Stream.of(
                Arguments.of("*/10/5"),
                Arguments.of("*//"),
                Arguments.of("//"),
                Arguments.of("13-20/3/1")
        );
    }

    @ParameterizedTest
    @MethodSource("lessThanOneStepFunctionProvider")
    @DisplayName("WHEN a component is a step function with a divisor less than 1 THEN an exception is thrown")
    void stepFunctionWithDivideByZeroErrors(String invalidComponent){
        ComponentCalculator componentCalculator = new CronComponentCalculator(0, 59);
        Exception e = Assertions.assertThrows(IllegalCronException.class, () -> componentCalculator.calculateComponent(invalidComponent));
        Assertions.assertEquals("Invalid Cron provided, unable to calculate step function: " + invalidComponent, e.getMessage());
    }

    private static Stream<Arguments> lessThanOneStepFunctionProvider(){
        return Stream.of(
                Arguments.of("*/0"),
                Arguments.of("10/0"),
                Arguments.of("10/-1"),
                Arguments.of("0/0")
        );
    }

    @Test
    @DisplayName("WHEN a component is a step function and the first segment is a single value THEN and exception is thrown")
    void stepFunctionWithIntegerAsSegmentOneErrors(){
        ComponentCalculator componentCalculator = new CronComponentCalculator(0, 59);
        Exception e = Assertions.assertThrows(IllegalCronException.class, () -> componentCalculator.calculateComponent("10/5"));
        Assertions.assertEquals("Invalid Cron provided, unable to calculate step function: " + "10/5", e.getMessage());
    }


    @ParameterizedTest
    @MethodSource("rangeFunctionProvider")
    @DisplayName("WHEN a component is a range THEN return the values within the range")
    void rangeFunctionReturnsCorrectSeries(List<Integer> expectedResult, String component){
        CronComponentCalculator cronComponentCalculator = new CronComponentCalculator(0, 59);
        Assertions.assertEquals(expectedResult, cronComponentCalculator.calculateComponent(component));
    }

    private static Stream<Arguments> rangeFunctionProvider(){
        return Stream.of(
                Arguments.of(List.of(5, 6, 7, 8, 9, 10), "5-10"),
                Arguments.of(IntStream.rangeClosed(0, 59).boxed().toList(), "0-59")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidRangeFunctionProvider")
    @DisplayName("WHEN a component is an invalid range THEN return throw an error unable to calculate range function")
    void invalidRangeFunctionThrowsException(String component){
        CronComponentCalculator cronComponentCalculator = new CronComponentCalculator(0, 59);
        Exception e = Assertions.assertThrows(IllegalCronException.class, () -> cronComponentCalculator.calculateComponent(component));
        Assertions.assertEquals("Invalid Cron provided, unable to calculate range function: " + component, e.getMessage());
    }

    private static Stream<Arguments> invalidRangeFunctionProvider(){
        return Stream.of(
                Arguments.of("5--10"),
                Arguments.of("0-59-4")
        );
    }

    @ParameterizedTest
    @MethodSource("listProvider")
    @DisplayName("WHEN a component is a list THEN return the values of the list")
    void listReturnsCorrectSeries(List<Integer> expectedResult, String component){
        CronComponentCalculator cronComponentCalculator = new CronComponentCalculator(0, 59);
        Assertions.assertEquals(expectedResult, cronComponentCalculator.calculateComponent(component));
    }

    private static Stream<Arguments> listProvider(){
        return Stream.of(
                Arguments.of(List.of(5, 6, 7, 10, 15, 32), "5,6,7,10,15,32"),
                Arguments.of(List.of(0,30,59), "0,30,59")
        );
    }

    @ParameterizedTest
    @MethodSource("allOperatorProvider")
    @DisplayName("WHEN a component is a list with other operators THEN the list shouldn't affect the other operators")
    void allOperatorsReturnCorrectSeries(List<Integer> expectedResult, String component){
        CronComponentCalculator cronComponentCalculator = new CronComponentCalculator(0, 59);
        Assertions.assertEquals(expectedResult, cronComponentCalculator.calculateComponent(component));
    }

    private static Stream<Arguments> allOperatorProvider(){
        return Stream.of(
                Arguments.of(List.of(0,1,2,3,4,5,14,15,16), "0-5,14,15,16"),
                Arguments.of(List.of(0,2,4,0,1,2,3,4,5,6,31), "0,2,4,0-5,6,31"),
                Arguments.of(List.of(0, 20,40,14,15,16), "*/20,14,15,16"),
                Arguments.of(List.of(10, 14, 18, 15, 59), "10-20/4,15,59"),
                Arguments.of(List.of(10, 14, 18, 15, 30, 40, 50), "10-20/4,15,30-50/10")

        );
    }

    @ParameterizedTest
    @MethodSource("singleValueProvider")
    @DisplayName("WHEN a component is a single value THEN the series should only contain the given value")
    void singleValueReturnsCorrectSeries(List<Integer> expectedResult, String component){
        CronComponentCalculator cronComponentCalculator = new CronComponentCalculator(0, 59);
        Assertions.assertEquals(expectedResult, cronComponentCalculator.calculateComponent(component));
    }

    private static Stream<Arguments> singleValueProvider(){
        return Stream.of(
                Arguments.of(List.of(0), "0"),
                Arguments.of(List.of(59), "59"),
                Arguments.of(List.of(31), "31")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidCharacterProvider")
    @DisplayName("WHEN a component is an invalid character THEN an IllegalCronException is thrown")
    void invalidCharacterThrowsException(String component){
        CronComponentCalculator cronComponentCalculator = new CronComponentCalculator(0, 59);
        Exception e = Assertions.assertThrows(IllegalCronException.class, () -> cronComponentCalculator.calculateComponent(component));
        Assertions.assertEquals("Invalid Cron provided, couldn't parse character: " + component, e.getMessage());
    }

    private static Stream<Arguments> invalidCharacterProvider(){
        return Stream.of(
                Arguments.of( "x"),
                Arguments.of( "-1"),
                Arguments.of("\""),
                Arguments.of( "10/x"),
                Arguments.of( "10-20/x"),
                Arguments.of( "10,12,13,x"),
                Arguments.of( "*x"),
                Arguments.of( "*/x12")
                );
    }
}