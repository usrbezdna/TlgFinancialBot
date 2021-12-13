import org.junit.*;
import com.gamedev.*;
import static org.junit.Assert.*;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import yahoofinance.YahooFinance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String expectedText = "This is help. We have:\n " +
                "\"/start\" - start bot and shows you keyboard of commands\n" +
                "\"/help\" - shows all commands that bot can do\n" +
                "\"/price\" - shows a price of an asset (USAGE: \"/price AAPL\")\n " +
                "\"add\" - adds ticker with certain amount to your portfolio (USAGE: \"/add AAPL 2\")\n" +
                "\"remove\" - removes ticker from your portfolio (USAGE: \"/remove AAPL\")\n" +
                "\"/npie\" - makes a pie diagram with amounts of each of your tickers\n" +
                "\"/pie\" - makes a pie diagram with costs of each your tickers\n" +
                "\"/balance\" - shows total cost of your portfolio\n" +
                " Try them all :)" +
                "NOTIFICATION: All prices, costs and totals are shown in USD.";
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

    @Test
    public void testPortfolioCalculation() {
        HashMap<String, String> testMap = new HashMap<String, String>(){{put("AAPL", "3"); put("AMD", "2");}};
        SendMessage real = GetPortfolioClass.calcPortfolioBalance(inputMessage, chatID, testMap, false);
        try {
            Double expected = Math.floor(GetPortfolioClass.getStockPriceUSD("AAPL") * 3
                    + GetPortfolioClass.getStockPriceUSD("AMD") * 2);
            assertEquals(real.getText(), "$" + expected.toString());
        }catch (Exception ignored){}
    }

    @Test
    public void testPortfolioCallback() {
        HashMap<String, String> testMap = new HashMap<String, String>(){{put("AAPL", "1"); put("AMD", "4");}};
        SendMessage real = GetPortfolioClass.calcPortfolioBalance(inputMessage, chatID, testMap, true);
        try {
            Double expectedAAPL = GetPortfolioClass.getStockPriceUSD("AAPL");
            Double expectedAMD =  GetPortfolioClass.getStockPriceUSD("AMD") * 4;
            Double total = expectedAAPL + expectedAMD;
            assertEquals(real.getText(), String.format("$%s\nAAPL - %s\nAMD - %s\n", Math.floor(total), expectedAAPL, expectedAMD));
        }catch (Exception ignored){}
    }

    @Test
    public void testKeyboard(){
        ReplyKeyboardMarkup real = KeyboardSetUpClass.setReplyKeyboard();
        List<KeyboardRow> expRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/help"); row.add("/pie"); row.add("/balance");
        expRows.add(row);
        ReplyKeyboardMarkup expected = new ReplyKeyboardMarkup();
        expected.setKeyboard(expRows);
        assertEquals(real, expected);
    }

    @Test
    public void testAdd(){
        JedisHandler.auth();
        Map<String, String> before = JedisHandler.getUserData(chatID);
        SendMessage messageForUser = AddAssetClass.addAsset(new String[]{"AAPL", "2"}, chatID);
        Map<String, String> after = JedisHandler.getUserData(chatID);
        assertEquals(messageForUser.getText(), "Added ticker AAPL with amount 2");
        assertNotEquals(before, after);
    }
}
