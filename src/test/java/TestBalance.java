import architecture.CommandContainer;
import architecture.ReturningValues;
import commands.Add;
import commands.Balance;
import commands.RemoveAll;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import utils.JedisHandler;
import utils.StockAPI;

import static org.junit.Assert.*;


@Slf4j
public class TestBalance {

    private final SendMessage inputMessage = new SendMessage();
    private final String chatID = "1337";
    private final String msgID = "7331";
    private final boolean callbackFlag = false;


    @Test
    public void testBalanceCorrect() {
        JedisHandler.auth();
        CommandContainer comCont = new CommandContainer("/balance".split("\\s"), callbackFlag, chatID, msgID);
        CommandContainer addAAPL = new CommandContainer("/add AAPL 3".split("\\s"), callbackFlag, chatID, msgID);
        CommandContainer addAMD = new CommandContainer("/add AMD 2".split("\\s"), callbackFlag, chatID, msgID);

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
                callbackFlag, chatID, msgID);
        assertTrue("Container should contain error", redundant.hasError());
        assertEquals("Container should have correct error message",
                "This command does not take any arguments", redundant.getErrorMessage());
    }

    @Test
    public void testBalanceCallback() {
        JedisHandler.auth();
        CommandContainer comCont = new CommandContainer("/balance detailed".split("\\s"),
                true, chatID, msgID);
        CommandContainer addAAPL = new CommandContainer("/add AAPL 1".split("\\s"), callbackFlag, chatID, msgID);
        CommandContainer addAMD = new CommandContainer("/add AMD 4".split("\\s"), callbackFlag, chatID, msgID);

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
