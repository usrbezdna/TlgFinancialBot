import architecture.CommandContainer;
import commands.Price;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import yahoofinance.YahooFinance;

import static org.junit.Assert.*;


@Slf4j
public class TestPrice {
    private final String chatID = "1337";
    private final String msgID = "7331";
    private final boolean callbackFlag = false;

    @Test
    public void testCorrectPriceCommand() {
        CommandContainer comCont = new CommandContainer("/price AAPL".split("\\s"), callbackFlag, chatID, msgID);
        String realText = Price.getPrice(comCont).getText();
        Double stockPrice = null;
        try {
            stockPrice = Math.round(
                    YahooFinance.get("AAPL")
                            .getQuote()
                            .getPrice()
                            .doubleValue() * 100.0) / 100.0;
        } catch (Exception e) {
            log.error("Error when getting price from exchange", e);
        }

        assertNotNull("Returned API object should not be null", stockPrice);
        String expectedText = "Found ticker with price " + stockPrice;
        assertEquals("Should find ticker with correct price", expectedText,realText);
    }

    @Test
    public void testPriceWithoutArgs() {
        CommandContainer comCont = new CommandContainer("/price".split("\\s"), callbackFlag, chatID, msgID);
        assertTrue("Command container should have error inside", comCont.hasError());
        assertEquals("Command container should contain correct error message",
                "Incorrect number of arguments, try \"/help\"", comCont.getErrorMessage());
    }

    @Test
    public void testPriceWithIncorrectTicker() {
        CommandContainer comCont = new CommandContainer("/price ZXC".split("\\s"), callbackFlag, chatID, msgID);
        Price price = new Price();
        price.validateArgs(comCont);
        assertTrue("Command container should have error inside", comCont.hasError());
        assertEquals("Command container should contain correct error message",
                "Invalid ticker. Please make sure that spelling is correct.", comCont.getErrorMessage());
    }
}
