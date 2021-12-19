import architecture.CommandContainer;
import commands.Add;
import commands.RemoveAll;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import utils.Diagram;
import utils.JedisHandler;
import utils.StockAPI;
import java.util.Map;
import static org.junit.Assert.*;

public class TestPie {

    private final SendMessage inputMessage = new SendMessage();
    private final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
    private final String chatID = "1337";
    private final String msgID = "7331";
    private final boolean callbackFlag = false;

    @Test
    public void testPricePie(){
        JedisHandler.auth();
        CommandContainer pricePie = new CommandContainer("/pie".split("\\s"),
                callbackFlag, chatID, msgID);
        CommandContainer addAMD = new CommandContainer("/add AMD 2".split("\\s"),
                callbackFlag, chatID, msgID);
        CommandContainer addAAPL = new CommandContainer("/add AAPL 4".split("\\s"),
                callbackFlag, chatID, msgID);

        assertFalse("Container should not have mistake", pricePie.hasError());
        assertFalse("Container should not have mistake", addAAPL.hasError());
        assertFalse("Container should not have mistake", addAMD.hasError());

        RemoveAll.removeAll(pricePie.getChatID());
        Add.addAsset(addAAPL);
        Add.addAsset(addAMD);

        Double expectedAAPL = StockAPI.getStockPriceUSD("AAPL");
        assertNotNull("Result of stock API should not be null", expectedAAPL);
        Double expectedAMD = StockAPI.getStockPriceUSD("AMD");
        assertNotNull("Result of stock API should not be null", expectedAMD);

        DefaultPieDataset expected = new DefaultPieDataset();
        expected.setValue("AAPL", expectedAAPL * 4);
        expected.setValue("AMD", expectedAMD * 2);
        Map<String, Integer> userData = JedisHandler.getUserData(pricePie.getChatID());
        assertNotNull("User data should not be null", userData);

        PieDataset real = Diagram.getPriceDataset(userData);
        assertEquals("Datasets should be equal", expected, real);
    }

    @Test
    public void testPricePieRedundant(){
        CommandContainer redundant = new CommandContainer("/pie qwe 123".split("\\s"),
                callbackFlag, chatID, msgID);
        assertTrue("Container should have error", redundant.hasError());
        assertEquals("", "This command does not take any arguments", redundant.getErrorMessage());
    }

    @Test
    public void testNumPie(){
        JedisHandler.auth();
        CommandContainer numPie = new CommandContainer("/npie".split("\\s"),
                callbackFlag, chatID, msgID);
        CommandContainer addAMD = new CommandContainer("/add AMD 2".split("\\s"),
                callbackFlag, chatID, msgID);
        CommandContainer addAAPL = new CommandContainer("/add AAPL 4".split("\\s"),
                callbackFlag, chatID, msgID);

        assertFalse("Container should not have mistake", numPie.hasError());
        assertFalse("Container should not have mistake", addAAPL.hasError());
        assertFalse("Container should not have mistake", addAMD.hasError());

        RemoveAll.removeAll(numPie.getChatID());
        Add.addAsset(addAAPL);
        Add.addAsset(addAMD);
        Map<String, Integer> userData = JedisHandler.getUserData(numPie.getChatID());
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
                callbackFlag, chatID, msgID);
        assertTrue("Container should have error", redundant.hasError());
        assertEquals("", "This command does not take any arguments", redundant.getErrorMessage());
    }
}
