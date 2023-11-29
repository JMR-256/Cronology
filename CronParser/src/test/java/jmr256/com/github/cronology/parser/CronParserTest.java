package jmr256.com.github.cronology.parser;

import jmr256.com.github.cronology.parser.componentcalculation.CronComponentCalculator;
import jmr256.com.github.cronology.parser.exceptions.IllegalCronException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CronParserTest {

    @Mock
    private CronComponentCalculator cronComponentCalculator;

    @InjectMocks
    private CronParser cronParser;

    @Test
    void parseReturnsErrorStringForIllegalCronException(){
        Mockito.when(cronComponentCalculator.calculateComponent(ArgumentMatchers.anyString())).thenThrow(new IllegalCronException("test_err"));
        String result = cronParser.parse("* * * * *");
        Assertions.assertEquals("test_err", result);
    }

    @Test
    void parseReturnsErrorStringForUnexpectedException(){
        String errMessage = "test_err";
        Mockito.when(cronComponentCalculator.calculateComponent(ArgumentMatchers.anyString())).thenThrow(new NullPointerException(errMessage));
        Exception e = Assertions.assertThrows(NullPointerException.class, () -> cronParser.parse("* * * * *"));
        Assertions.assertEquals(errMessage, e.getMessage());
    }

    @Test
    void parseCatchesExceptionForExpressionLessThan5Components(){
        String invalidExpression = "* * * *";
        Assertions.assertEquals("Invalid parameter format: " + invalidExpression, cronParser.parse(invalidExpression));
    }

    @Test
    void parseCatchesExceptionForExpressionMoreThan6Components(){
        String invalidExpression = "* * * * * * *";
        Assertions.assertEquals("Invalid parameter format: " + invalidExpression, cronParser.parse(invalidExpression));
    }
}