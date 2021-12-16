import com.gamedev.*;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import yahoofinance.YahooFinance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestBot {

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
        "\"/start\" - starts bot and shows you keyboard of commands\n" +
                "\"/help\" - shows all commands that bot can do\n" +
                "\"/price\" - shows a price of an asset (USAGE: \"/price AAPL\")\n " +
                "\"/add\" - adds ticker with certain amount to your portfolio (USAGE: \"/add AAPL 2\")\n" +
                "\"/remove\" - removes ticker from your portfolio (USAGE: \"/remove AAPL\")\n" +
                "\"/npie\" - makes a pie diagram with amounts of each of your tickers\n" +
                "\"/pie\" - makes a pie diagram with costs of each your tickers\n" +
                "\"/balance\" - shows total cost of your portfolio\n" +
                " Try them all :)\n" +
                "NOTIFICATION: All prices, costs and totals are shown in USD.";
        assertEquals(realText, expectedText);
    }

    @Test
    public void testCorrectPriceCommand() {
        CommandContainer comCont = new CommandContainer("/price AAPL".split("\\s"), chatID);
        String realText = GetStockPrice.getPrice(comCont).getText();
        String stockPrice = null;
        try {
            stockPrice = YahooFinance.get("AAPL").toString();
        } catch (Exception ignored) {} //TODO

        String expectedText = "Found ticker with price " + stockPrice;
        assertEquals(realText, expectedText);
    }

    @Test
    public void testIncorrectPriceCommand() {
        CommandContainer comCont = new CommandContainer("/price QTC".split("\\s"), chatID);
        String realText = GetStockPrice.getPrice(comCont).getText();
        String expectedText = "Can`t find current ticker, try again please";
        assertEquals(realText, expectedText);
    }

    @Test
    public void testPortfolioCalculation() {
        HashMap<String, Integer> testMap = new HashMap<String, Integer>(){{put("AAPL", 3); put("AMD", 2);}};
        SendMessage real = GetPortfolio.calcPortfolioBalance(inputMessage, chatID, testMap, false);
        try {
            double expected = Math.floor(StockAPI.getStockPriceUSD("AAPL") * 3
                    + StockAPI.getStockPriceUSD("AMD") * 2);
            assertEquals(real.getText(), "$" + expected);
        }catch (Exception ignored){} //TODO
    }

    @Test
    public void testPortfolioCallback() {
        HashMap<String, Integer> testPortfolio = new HashMap<String, Integer>(){{put("AAPL", 1); put("AMD", 4);}};
        SendMessage real = GetPortfolio.calcPortfolioBalance(inputMessage, chatID, testPortfolio, true);
        try {

            double expectedAAPL = StockAPI.getStockPriceUSD("AAPL");
            double expectedAMD =  StockAPI.getStockPriceUSD("AMD") * 4;
            double total = expectedAAPL + expectedAMD;

            assertEquals(real.getText(), String.format("$%s\nAAPL - %s\nAMD - %s\n", Math.floor(total),
                    Math.floor(expectedAAPL), Math.floor(expectedAMD)));
        } catch (Exception ignored){} //TODO
    }

    @Test
    public void testKeyboard(){
        ReplyKeyboardMarkup real = KeyboardSetUp.setReplyKeyboard();
        List<KeyboardRow> expRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/help"); row.add("/pie"); row.add("/balance");
        expRows.add(row);
        ReplyKeyboardMarkup expected = new ReplyKeyboardMarkup();
        expected.setKeyboard(expRows);
        assertEquals(real, expected);
    }
}
