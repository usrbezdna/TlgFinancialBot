import com.gamedev.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import yahoofinance.YahooFinance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@Slf4j
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

        String expectedText = "Found ticker with price " + stockPrice.toString();
        assertEquals(realText, expectedText);
    }

    @Test
    public void testIncorrectPriceCommand() {
        CommandContainer comCont = new CommandContainer("/price QTC".split("\\s"), chatID);
        String realText = GetStockPrice.getPrice(comCont).getText();
        String expectedText = "Invalid ticker. Please make sure that spelling is correct.";
        assertEquals(realText, expectedText);
    }


    @Test
    public void testPortfolioCalculation() {
        HashMap<String, Integer> testMap = new HashMap<String, Integer>(){{put("AAPL", 3); put("AMD", 2);}};
        SendMessage real = GetPortfolio.calcPortfolioBalance(inputMessage, chatID, testMap, false);
        try {
            double expected = Math.round((StockAPI.getStockPriceUSD("AAPL") * 3
                    + StockAPI.getStockPriceUSD("AMD") * 2) * 100.0) / 100.0;
            assertEquals(real.getText(), "$" + expected);
        } catch (Exception e){
            log.error("Error when getting price from exchange", e);
        }
    }

    @Test
    public void testPortfolioCallback() {
        HashMap<String, Integer> testPortfolio = new HashMap<String, Integer>(){{put("AAPL", 1); put("AMD", 4);}};
        SendMessage real = GetPortfolio.calcPortfolioBalance(inputMessage, chatID, testPortfolio, true);
        try {

            double expectedAAPL = StockAPI.getStockPriceUSD("AAPL");
            double expectedAMD =  StockAPI.getStockPriceUSD("AMD") * 4;
            double total = expectedAAPL + expectedAMD;

            assertEquals(real.getText(), String.format("$%s\nAAPL - $%s (1 pcs)\nAMD - $%s (4 pcs)\n", 
                    Math.round(total * 100) / 100.0,
                    Math.round(expectedAAPL * 100) / 100.0, 
                    Math.round(expectedAMD * 100) / 100.0
                ));
        } catch (Exception e){
            log.error("Error when getting price from exchange", e);
        }
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

    @Test
    public void testAdd(){
        CommandContainer comCont = new CommandContainer("/add AAPL 2".split("\\s"), chatID);
        JedisHandler.auth();
        Map<String, Integer> before = JedisHandler.getUserData(chatID);
        SendMessage messageForUser = AddAsset.addAsset(comCont);
        Map<String, Integer> after = JedisHandler.getUserData(chatID);
        assertEquals(messageForUser.getText(), "Added ticker AAPL with amount: 2");
        assertNotEquals(before, after);
    }

    @Test
    public void testAddIncorrectAmount(){
        CommandContainer comCont = new CommandContainer("/add AAPL qwe".split("\\s"), chatID);
        SendMessage messageForUser = AddAsset.addAsset(comCont);
        assertEquals(messageForUser.getText(), "Please specify correct amount");
    }

    @Test
    public void testAddIncorrectTicker(){
        CommandContainer comCont = new CommandContainer("/add QwerTY 12".split("\\s"), chatID);
        SendMessage messageForUser = AddAsset.addAsset(comCont);
        assertEquals(messageForUser.getText(), "Invalid ticker. Please make sure that spelling is correct.");
    }

    @Test
    public void testAddWithoutAmount(){
        CommandContainer comCont = new CommandContainer("/add AAPL".split("\\s"), chatID);
        SendMessage messageForUser = AddAsset.addAsset(comCont);
        assertEquals(messageForUser.getText(), "Please specify number of assets");
    }

    @Test
    public void testRemove(){
        CommandContainer comCont = new CommandContainer("/remove AMD".split("\\s"), chatID);
        JedisHandler.auth();
        Map<String, Integer> before = JedisHandler.getUserData(chatID);
        AddAsset.addAsset(new CommandContainer("/add AMD 30".split("\\s"), chatID));
        SendMessage messageForUser = RemoveAsset.removeAsset(comCont);
        Map<String, Integer> after = JedisHandler.getUserData(chatID);
        assertEquals(messageForUser.getText(), "Removed ticker AMD");
        assertEquals(before, after);
    }

    @Test
    public void testRemoveIncorrect(){
        CommandContainer comCont = new CommandContainer("/remove ZXCghoul".split("\\s"), chatID);
        JedisHandler.auth();
        AddAsset.addAsset(new CommandContainer("/add AAPL 10".split("\\s"), chatID));
        SendMessage messageForUser = RemoveAsset.removeAsset(comCont);
        assertEquals(messageForUser.getText(), "There's no such ticker in your portfolio.");
    }

    @Test
    public void testRemoveEmpty(){
        CommandContainer comCont = new CommandContainer("/remove AMD".split("\\s"), chatID);
        JedisHandler.auth();
        JedisHandler.removeAll(chatID);
        SendMessage messageForUser = RemoveAsset.removeAsset(comCont);
        assertEquals(messageForUser.getText(), "Your portfolio is empty. Nothing to remove.");
    }

}
