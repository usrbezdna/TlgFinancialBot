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

import redis.clients.jedis.Jedis;

@Slf4j
public class TestBot {

    private final SendMessage inputMessage = new SendMessage();
    private final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
    private final String chatID = "1337";
    private final String msgID = "7331";
    private final boolean callbackFlag = false;


    @Test
    public void testStartCommand() {
        String realText = Start.start(inputMessage, chatID, keyboard).getText();
        String expectedText = "Started! Try \"/help\" for commands.";
        assertEquals(realText, expectedText);
    }

    @Test
    public void testHelpCommand() {
        String realText = Help.help(inputMessage, chatID).getText();
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
        CommandContainer comCont = new CommandContainer("/price AAPL".split("\\s"),
                                    callbackFlag, chatID, msgID);
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

        assert stockPrice != null;
        String expectedText = "Found ticker with price " + stockPrice;
        assertEquals(realText, expectedText);
    }

    @Test
    public void testIncorrectPriceCommand() {
        CommandContainer comCont = new CommandContainer("/price QTC".split("\\s"),
                                    callbackFlag, chatID, msgID);
        new Price().validateArgs(comCont);
        String expectedText = "Invalid ticker. Please make sure that spelling is correct.";
        assertEquals(comCont.getErrorMessage(), expectedText);
    }


    @Test
    public void testPortfolioCalculation() {
        HashMap<String, Integer> testMap = new HashMap<String, Integer>(){{put("AAPL", 3); put("AMD", 2);}};
        SendMessage real = GetPortfolio.calcPortfolioBalance(inputMessage, chatID, testMap, false);

        Double aaplPrice = StockAPI.getStockPriceUSD("AAPL");
        Double amdPrice = StockAPI.getStockPriceUSD("AMD");

        assert aaplPrice != null;
        assert amdPrice != null;

        double expected = Math.round((aaplPrice * 3 + amdPrice * 2) * 100.0) / 100.0;
        assertEquals(real.getText(), "$" + expected);
    }

    @Test
    public void testPortfolioCallback() {
        HashMap<String, Integer> testPortfolio = new HashMap<String, Integer>(){{put("AAPL", 1); put("AMD", 4);}};
        SendMessage real = GetPortfolio.calcPortfolioBalance(inputMessage, chatID, testPortfolio, true);

        Double aaplPrice = StockAPI.getStockPriceUSD("AAPL");
        Double amdPrice = StockAPI.getStockPriceUSD("AMD");

        assert aaplPrice != null;
        assert amdPrice != null;

        amdPrice = amdPrice * 4;
        double total = aaplPrice + amdPrice ;

        assertEquals(real.getText(), String.format("$%s\nAAPL - $%s (1 pcs)\nAMD - $%s (4 pcs)\n",
                Math.round(total * 100) / 100.0,
                Math.round(aaplPrice * 100) / 100.0,
                Math.round(amdPrice * 100) / 100.0
        ));
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
        CommandContainer comCont = new CommandContainer("/add AAPL 2".split("\\s"),
                                    callbackFlag, chatID, msgID);
        JedisHandler.auth();
        Map<String, Integer> before = JedisHandler.getUserData(chatID);
        SendMessage messageForUser = Add.addAsset(comCont);

        Map<String, Integer> after = JedisHandler.getUserData(chatID);
        assertEquals(messageForUser.getText(), "Added ticker AAPL with amount: 2");

        assertNotEquals(before, after);
    }

    @Test
    public void testAddIncorrectAmount(){
        CommandContainer comCont = new CommandContainer("/add AAPL qwe".split("\\s"),
                                    callbackFlag, chatID, msgID);
        new Add().validateArgs(comCont);
        assertEquals(comCont.getErrorMessage(), "Please specify correct amount");
    }

    @Test
    public void testAddIncorrectTicker(){
        CommandContainer comCont = new CommandContainer("/add QwerTY 12".split("\\s"),
                                    callbackFlag, chatID, msgID);
        new Add().validateArgs(comCont);
        assertEquals(comCont.getErrorMessage(), "Invalid ticker. Please make sure that spelling is correct.");
    }


    @Test
    public void testRemove(){

        JedisHandler.auth();

        CommandContainer comCont = new CommandContainer("/remove AMD".split("\\s"),
                                    callbackFlag, chatID, msgID);
        Map<String, Integer> before = JedisHandler.getUserData(chatID);

        Add.addAsset(new CommandContainer("/add AMD 30".split("\\s"),
                                    callbackFlag, chatID, msgID));
        SendMessage messageForUser = Remove.removeAsset(comCont);

        Map<String, Integer> after = JedisHandler.getUserData(chatID);

        assertEquals(messageForUser.getText(), "Removed ticker AMD");
        assertEquals(before, after);
    }

    @Test
    public void testRemoveIncorrect(){
        JedisHandler.auth();

        CommandContainer comCont = new CommandContainer("/remove ZXCghoul".split("\\s"),
                                    callbackFlag, chatID, msgID);

        Add.addAsset(new CommandContainer("/add AAPL 10".split("\\s"),
                                    callbackFlag, chatID, msgID));
        SendMessage messageForUser = Remove.removeAsset(comCont);
        assertEquals(messageForUser.getText(), "There's no such ticker in your portfolio.");
    }

    @Test
    public void testRemoveEmpty(){
        JedisHandler.auth();

        CommandContainer comCont = new CommandContainer("/remove AMD".split("\\s"),
                                     callbackFlag, chatID, msgID);
        JedisHandler.removeAll(chatID);

        SendMessage messageForUser = Remove.removeAsset(comCont);
        assertEquals(messageForUser.getText(), "Your portfolio is empty. Nothing to remove.");
    }
}
