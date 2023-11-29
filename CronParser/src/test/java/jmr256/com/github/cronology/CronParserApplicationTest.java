package jmr256.com.github.cronology;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


class CronParserApplicationTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private static final String INVALID_NUM_ARGS = "Invalid arguments supplied please use format: \"* * * * * /usr/bin/someCommand\"";


    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void cronParserShouldErrorForMoreThan1Arg(){
        CronParserApplication.main(new String[]{"* * * * * usr/bin/grep", "additional arg"});
        Assertions.assertEquals(INVALID_NUM_ARGS, errContent.toString().trim());
    }
}