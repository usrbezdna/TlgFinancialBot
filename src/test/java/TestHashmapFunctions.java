import org.junit.*;
import com.gamedev.*;
import static org.junit.Assert.*;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import yahoofinance.YahooFinance;

public class TestHashmapFunctions {

    private final SendMessage inputMessage = new SendMessage();
    private final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
    private final String chatID = "1337";


    @Test
    public void testStartCommand() {
        String realText = StartCommand.start(inputMessage, chatID, keyboard).getText();
        String expectedText = "Started! Try \"/help\" for commands.";
        assertEquals(realText, expectedText);
    }

    @Test
    public void testHelpCommand() {
        String realText = HelpCommand.help(inputMessage, chatID).getText();
        String expectedText = "This is help. We have \"/pie\" - makes a test " +
                "pie diagram, \"/help\" - helps and \"/start\" - starts and opens " +
                "inline keyboard. And also you can get a price of asset, just type:" +
                " \"/price AAPL\". Try them all :)";
        assertEquals(realText, expectedText);
    }

    @Test
    public void testCorrectPriceCommand() {
        String realText = GetStockPrice.getPrice(inputMessage, chatID, "AAPL").getText();
        String stockPrice = null;
        try {
            stockPrice = YahooFinance.get("AAPL").toString();
        } catch (Exception ignored) {}

        String expectedText = "Found ticker with price " + stockPrice;
        assertEquals(realText, expectedText);
    }

    @Test
    public void testIncorrectPriceCommand() {
        String realText = GetStockPrice.getPrice(inputMessage, chatID, "QTC").getText();
        String expectedText = "Can`t find current ticker, try again please";
        assertEquals(realText, expectedText);
    }
}
