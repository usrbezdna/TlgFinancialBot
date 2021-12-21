import architecture.CommandContainer;
import commands.Add;
import commands.RemoveAll;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.clients.jedis.Jedis;
import utils.Diagram;
import utils.JedisHandler;
import utils.StockAPI;

import java.util.Map;

import static org.junit.Assert.*;


@Testcontainers
@RunWith(JUnit4.class)
public class TestPie {

    private final SendMessage message = new SendMessage();
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
    public void testPricePieRedundant(){
        CommandContainer redundant = new CommandContainer("/pie qwe 123".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        assertTrue("Container should have error", redundant.hasError());
        assertEquals("Should send correct error message",
                "This command does not take any arguments", redundant.getErrorMessage());
    }

    @Test
    public void testPricePie(){

        CommandContainer addAMD = new CommandContainer("/add AMD 2".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        CommandContainer addAAPL = new CommandContainer("/add AAPL 4".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);

        assertFalse("Container should not have mistake", addAAPL.hasError());
        assertFalse("Container should not have mistake", addAMD.hasError());

        Add.addAsset(addAAPL);
        Add.addAsset(addAMD);

        CommandContainer pricePie = new CommandContainer("/pie".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        assertFalse("Container should not have mistake", pricePie.hasError());



        Double expectedAAPL = StockAPI.getStockPriceUSD("AAPL");
        assertNotNull("Result of stock API should not be null", expectedAAPL);
        Double expectedAMD = StockAPI.getStockPriceUSD("AMD");
        assertNotNull("Result of stock API should not be null", expectedAMD);

        DefaultPieDataset expected = new DefaultPieDataset();
        expected.setValue("AAPL", expectedAAPL * 4);
        expected.setValue("AMD", expectedAMD * 2);
        Map<String, Integer> userData = JedisHandler.getUserData(pricePie.getChatID(), testDataBase);
        assertNotNull("User data should not be null", userData);

        PieDataset real = Diagram.getPriceDataset(userData);
        assertEquals("Datasets should be equal", expected, real);
    }



    @Test
    public void testNumPie(){

        CommandContainer addAMD = new CommandContainer("/add AMD 2".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        CommandContainer addAAPL = new CommandContainer("/add AAPL 4".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);

        assertFalse("Container should not have mistake", addAAPL.hasError());
        assertFalse("Container should not have mistake", addAMD.hasError());

        RemoveAll.removeAll(addAAPL, message);
        Add.addAsset(addAAPL);
        Add.addAsset(addAMD);

        CommandContainer numPie = new CommandContainer("/npie".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        assertFalse("Container should not have mistake", numPie.hasError());



        Map<String, Integer> userData = JedisHandler.getUserData(numPie.getChatID(), testDataBase);
        assertNotNull("User data should not be null", userData);


        Integer expectedAMD = 2;
        Integer expectedAAPL = 4;
        DefaultPieDataset expected = new DefaultPieDataset();
        expected.setValue("AAPL", expectedAAPL);
        expected.setValue("AMD", expectedAMD);

        PieDataset real = Diagram.getNumDataset(userData);

        assertEquals("Datasets should be equal", expected, real);
    }

    @Test
    public void testNumPieRedundant(){
        CommandContainer redundant = new CommandContainer("/npie qwe 123".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        assertTrue("Container should have error", redundant.hasError());
        assertEquals("Should send correct error message",
                "This command does not take any arguments", redundant.getErrorMessage());
    }
}
