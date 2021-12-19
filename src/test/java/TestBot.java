import architecture.CommandContainer;
import commands.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import utils.*;
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
    public void testStart() {
        String realText = Start.start(inputMessage, chatID, keyboard).getText();
        String expectedText = "Started! Try \"/help\" for commands.";
        assertEquals("Should send correct message", expectedText, realText);
    }

    @Test
    public void testHelp() {
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
        assertEquals( "Should provide correct help", expectedText, realText);
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
        assertEquals("Should provide correct keyboard", expected, real);
    }
}
