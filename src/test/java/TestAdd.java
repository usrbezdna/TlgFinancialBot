import architecture.CommandContainer;
import commands.Add;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import utils.JedisHandler;
import java.util.Map;
import static org.junit.Assert.*;

@Slf4j
public class TestAdd{
    private final String chatID = "1337";
    private final String msgID = "7331";
    private final boolean callbackFlag = false;

    @Test
    public void testCorrectAdd(){
        CommandContainer comCont = new CommandContainer("/add AAPL 2".split("\\s"), callbackFlag, chatID, msgID);
        JedisHandler.auth();

        Map<String, Integer> before = JedisHandler.getUserData(chatID);
        SendMessage messageForUser = Add.addAsset(comCont);

        Map<String, Integer> after = JedisHandler.getUserData(chatID);
        assertEquals( "Message should contain correct text",
                "Added ticker AAPL with amount: 2", messageForUser.getText());

        assertNotEquals("User's portfolio should change after adding an asset", before, after);
    }

    @Test
    public void testAddIncorrectNumberOfArgs() {
        CommandContainer missingOne = new CommandContainer("/add AAPL".split("\\s"), callbackFlag, chatID, msgID);
        CommandContainer missingTwo = new CommandContainer("/add".split("\\s"), callbackFlag, chatID, msgID);

        assertTrue("Command container with one missing argument should have error inside",
                missingOne.hasError());
        assertTrue("Command container with two missing arguments should have error inside",
                missingTwo.hasError());
        assertEquals("Command container with one missing argument should contain correct error message",
                "Incorrect number of arguments, try \"/help\"", missingOne.getErrorMessage());
        assertEquals("Command container with two missing arguments should contain correct error message",
                "Incorrect number of arguments, try \"/help\"", missingTwo.getErrorMessage());
    }

    @Test
    public void testAddIncorrectAmount(){
        CommandContainer notNumber = new CommandContainer("/add AAPL qwe".split("\\s"),
                callbackFlag, chatID, msgID);
        CommandContainer fractionalAmount = new CommandContainer("/add AAPL 12.7".split("\\s"),
                callbackFlag, chatID, msgID);
        CommandContainer negativeAmount = new CommandContainer("/add AAPL -15".split("\\s"),
                callbackFlag, chatID, msgID);
        CommandContainer typoMistake = new CommandContainer("/add AAPL 12w3".split("\\s"),
                callbackFlag, chatID, msgID);

        Add add = new Add();

        add.validateArgs(notNumber);
        add.validateArgs(fractionalAmount);
        add.validateArgs(negativeAmount);
        add.validateArgs(typoMistake);

        assertTrue("Command container should have error inside", notNumber.hasError());
        assertTrue("Command container should have error inside", fractionalAmount.hasError());
        assertTrue("Command container should have error inside", negativeAmount.hasError());
        assertTrue("Command container should have error inside", typoMistake.hasError());

        assertEquals("Container should have correct error message",
                 "Please specify correct amount", notNumber.getErrorMessage());
        assertEquals("Container should have correct error message",
                "Please specify correct amount", fractionalAmount.getErrorMessage());
        assertEquals("Container should have correct error message",
                "Please specify correct amount", negativeAmount.getErrorMessage());
        assertEquals("Container should have correct error message",
                "Please specify correct amount", typoMistake.getErrorMessage());
    }

    @Test
    public void testAddIncorrectTicker(){
        CommandContainer comCont = new CommandContainer("/add QwerTY 12".split("\\s"),
                callbackFlag, chatID, msgID);
        new Add().validateArgs(comCont);
        assertTrue("Container should have error inside", comCont.hasError());
        assertEquals( "Container should have correct error message",
                "Invalid ticker. Please make sure that spelling is correct.", comCont.getErrorMessage());
    }
}
