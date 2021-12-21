import architecture.CommandContainer;
import architecture.ReturningValues;
import commands.Add;
import commands.Balance;
import commands.RemoveAll;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.clients.jedis.Jedis;
import utils.JedisHandler;
import utils.StockAPI;

import static org.junit.Assert.*;


@Testcontainers
@RunWith(JUnit4.class)
public class TestBalance {

    private final SendMessage inputMessage = new SendMessage();
    private final String chatID = "1337";
    private final String msgID = "7331";
    private final boolean callbackFlag = false;


    private static Jedis testDataBase;

    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:3-alpine")
            .withExposedPorts(6379).withReuse(true);

    @BeforeClass
    public static void setUp() {
        redisContainer.start();

        String contHost = redisContainer.getHost();
        Integer contPort = redisContainer.getMappedPort(6379);

        testDataBase = new Jedis(contHost, contPort);
    }


    @Test
    public void testBalanceCorrect() {
        CommandContainer comCont = new CommandContainer("/balance".split("\\s"), callbackFlag, chatID, msgID, testDataBase);
        CommandContainer addAAPL = new CommandContainer("/add AAPL 3".split("\\s"), callbackFlag, chatID, msgID, testDataBase);
        CommandContainer addAMD = new CommandContainer("/add AMD 2".split("\\s"), callbackFlag, chatID, msgID, testDataBase);

        assertFalse("Container should not have error", comCont.hasError());
        assertFalse("Container should not have error", addAAPL.hasError());
        assertFalse("Container should not have error", addAMD.hasError());

        RemoveAll.removeAll(comCont, inputMessage);
        Add.addAsset(addAAPL);
        Add.addAsset(addAMD);

        Double aaplPrice = StockAPI.getStockPriceUSD("AAPL");
        Double amdPrice = StockAPI.getStockPriceUSD("AMD");

        assertNotNull("Return of stock API should not be null", aaplPrice);
        assertNotNull("Return of stock API should not be null", amdPrice);

        ReturningValues messageForUser = Balance.getBalance(comCont, new SendMessage(), new EditMessageText());

        double expected = Math.round((aaplPrice * 3 + amdPrice * 2) * 100.0) / 100.0;

        assertEquals( "Should return correct message with correct balance",
                "$" + expected, messageForUser._message_.getText());
    }

    @Test
    public void testBalanceRedundantArgs(){
        CommandContainer redundant = new CommandContainer("/balance 123 qwe".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        assertTrue("Container should contain error", redundant.hasError());
        assertEquals("Container should have correct error message",
                "This command does not take any arguments", redundant.getErrorMessage());
    }

    @Test
    public void testBalanceCallback() {
        CommandContainer comCont = new CommandContainer("/balance detailed".split("\\s"),
                true, chatID, msgID, testDataBase);
        CommandContainer addAAPL = new CommandContainer("/add AAPL 1".split("\\s"), callbackFlag, chatID, msgID, testDataBase);
        CommandContainer addAMD = new CommandContainer("/add AMD 4".split("\\s"), callbackFlag, chatID, msgID, testDataBase);

        assertFalse("Container should not have error", comCont.hasError());
        assertFalse("Container should not have error", addAAPL.hasError());
        assertFalse("Container should not have error", addAMD.hasError());

        RemoveAll.removeAll(comCont, inputMessage);
        Add.addAsset(addAAPL);
        Add.addAsset(addAMD);

        Double aaplPrice = StockAPI.getStockPriceUSD("AAPL");
        Double amdPrice = StockAPI.getStockPriceUSD("AMD");

        assertNotNull("Return of stock API should not be null", aaplPrice);
        assertNotNull("Return of stock API should not be null", amdPrice);

        amdPrice = amdPrice * 4;
        double total = aaplPrice + amdPrice ;

        ReturningValues messageForUser = Balance.getBalance(comCont, new SendMessage(), new EditMessageText());

        assertEquals("Should return correct detailed portfolio",
                String.format("$%s\nAAPL - $%s (1 pcs)\nAMD - $%s (4 pcs)\n",
                Math.round(total * 100) / 100.0,
                Math.round(aaplPrice * 100) / 100.0,
                Math.round(amdPrice * 100) / 100.0
        ), messageForUser._edited_message_.getText());
    }
}
