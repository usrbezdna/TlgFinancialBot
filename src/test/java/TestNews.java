import architecture.CommandContainer;
import commands.News;
import commands.PortfolioNews;
import commands.RemoveAll;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import utils.JedisHandler;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestNews {
    private final SendMessage inputMessage = new SendMessage();
    private final String chatID = "1337";
    private final String msgID = "7331";
    private final boolean callbackFlag = false;

    @Test
    public void testNewsIncorrectTicker(){
        CommandContainer incorrect = new CommandContainer("/news ZXC".split("\\s"),
                callbackFlag, chatID, msgID);
        new News().validateArgs(incorrect);
        assertTrue("Container should have error", incorrect.hasError());
        assertEquals("Should send correct error message",
                "Invalid ticker. Please make sure that spelling is correct.", incorrect.getErrorMessage());
    }

    @Test
    public void testEmptyPortfolio(){
        JedisHandler.auth();
        CommandContainer empty = new CommandContainer("/portfolioNews".split("\\s"),
                callbackFlag, chatID, msgID);
        RemoveAll.removeAll(empty, inputMessage);
        new PortfolioNews().validateArgs(empty);
        assertTrue("Container should have error", empty.hasError());
        assertEquals("Should send correct error message",
                "Your portfolio is empty", empty.getErrorMessage());
    }
}
