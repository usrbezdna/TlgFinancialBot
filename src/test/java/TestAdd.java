import architecture.CommandContainer;
import commands.Add;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.clients.jedis.Jedis;
import utils.JedisHandler;

import java.util.Map;

import static org.junit.Assert.*;

@Testcontainers
@RunWith(JUnit4.class)
public class TestAdd{

    private final String chatID = "1337";
    private final String msgID = "7331";
    private final boolean callbackFlag = false;
    private static Jedis testDataBase;

    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:3-alpine")
            .withExposedPorts(6379);

    @BeforeClass
    public static void setUp() {
        redisContainer.start();
        String contHost = redisContainer.getHost();
        Integer contPort = redisContainer.getMappedPort(6379);
        testDataBase = new Jedis(contHost, contPort);
    }


    @Test
    public void testCorrectAdd(){
        CommandContainer comCont = new CommandContainer("/add AAPL 2".split("\\s"),
                                            callbackFlag, chatID, msgID, testDataBase);

        Map<String, Integer> before = JedisHandler.getUserData(chatID, testDataBase);
        SendMessage messageForUser = Add.addAsset(comCont);
        Map<String, Integer> after = JedisHandler.getUserData(chatID, testDataBase);

        assertEquals( "Message should contain correct text",
                "Added ticker AAPL with amount: 2", messageForUser.getText());

        assertNotEquals("User's portfolio should change after adding an asset", before, after);
    }

    @Test
    public void testAddIncorrectNumberOfArgs() {
        CommandContainer missingOne = new CommandContainer("/add AAPL".split("\\s"), callbackFlag, chatID,
                                                                     msgID, testDataBase);
        CommandContainer missingTwo = new CommandContainer("/add".split("\\s"), callbackFlag, chatID,
                                                                     msgID, testDataBase);

        assertTrue("Command container with one missing argument should have error inside",
                missingOne.hasError());
        assertTrue("Command container with two missing arguments should have error inside",
                missingTwo.hasError());
        assertEquals("Command container with one missing argument should contain correct error message",
                "Incorrect number of arguments, try \"/help\"", missingOne.getErrorMessage());
        assertEquals("Command container with two missing arguments should contain correct error message",
                "Incorrect number of arguments, try \"/help\"", missingTwo.getErrorMessage());
    }

    @Test
    public void testAddIncorrectAmount(){
        CommandContainer notNumber = new CommandContainer("/add AAPL qwe".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        CommandContainer fractionalAmount = new CommandContainer("/add AAPL 12.7".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        CommandContainer negativeAmount = new CommandContainer("/add AAPL -15".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        CommandContainer typoMistake = new CommandContainer("/add AAPL 12w3".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);

        Add add = new Add();

        add.validateArgs(notNumber);
        add.validateArgs(fractionalAmount);
        add.validateArgs(negativeAmount);
        add.validateArgs(typoMistake);

        assertTrue("Command container should have error inside", notNumber.hasError());
        assertTrue("Command container should have error inside", fractionalAmount.hasError());
        assertTrue("Command container should have error inside", negativeAmount.hasError());
        assertTrue("Command container should have error inside", typoMistake.hasError());

        assertEquals("Container should have correct error message",
                 "Please specify correct amount", notNumber.getErrorMessage());
        assertEquals("Container should have correct error message",
                "Please specify correct amount", fractionalAmount.getErrorMessage());
        assertEquals("Container should have correct error message",
                "Please specify correct amount", negativeAmount.getErrorMessage());
        assertEquals("Container should have correct error message",
                "Please specify correct amount", typoMistake.getErrorMessage());
    }

    @Test
    public void testAddIncorrectTicker(){
        CommandContainer comCont = new CommandContainer("/add QwerTY 12".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        new Add().validateArgs(comCont);
        assertTrue("Container should have error inside", comCont.hasError());
        assertEquals( "Container should have correct error message",
                "Invalid ticker. Please make sure that spelling is correct.", comCont.getErrorMessage());
    }
}
