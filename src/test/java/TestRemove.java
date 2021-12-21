import architecture.CommandContainer;
import commands.Add;
import commands.Remove;
import commands.RemoveAll;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import utils.JedisHandler;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

@Slf4j
public class TestRemove {
    private final SendMessage inputMessage = new SendMessage();
    private final String chatID = "1337";
    private final String msgID = "7331";
    private final boolean callbackFlag = false;

    @Test
    public void testRemoveCorrect(){
        JedisHandler.auth();

        CommandContainer comCont = new CommandContainer("/remove AMD".split("\\s"),
                callbackFlag, chatID, msgID);
        new Remove().validateArgs(comCont);

        assertFalse("Container should not have error", comCont.hasError());

        Map<String, Integer> before = JedisHandler.getUserData(chatID);
        Add.addAsset(new CommandContainer("/add AMD 30".split("\\s"),
                callbackFlag, chatID, msgID));
        SendMessage messageForUser = Remove.removeAsset(comCont);
        Map<String, Integer> after = JedisHandler.getUserData(chatID);

        assertEquals( "Message should contain correct text",
                "Removed ticker AMD", messageForUser.getText());
        assertEquals("User portfolio should be equal after combination \"add + remove\"", before, after);
    }

    @Test
    public void testRemoveWithoutArgument() {
        CommandContainer withoutArgs = new CommandContainer("/remove".split("\\s"), callbackFlag, chatID, msgID);
        assertTrue("Container should have error", withoutArgs.hasError());
        assertEquals("Should return correct error message",
                "Incorrect number of arguments, try \"/help\"", withoutArgs.getErrorMessage());
    }

    @Test
    public void testRemoveIncorrectArgument(){
        JedisHandler.auth();

        CommandContainer comCont = new CommandContainer("/remove ZXCghoul".split("\\s"),
                callbackFlag, chatID, msgID);
        Add.addAsset(new CommandContainer("/add AMD 15".split("\\s"), callbackFlag, chatID, msgID));
        SendMessage messageForUser = Remove.removeAsset(comCont);
        assertEquals("Should return correct error message for user",
                "There's no such ticker in your portfolio.", messageForUser.getText());
    }

    @Test
    public void testRemoveEmpty(){
        JedisHandler.auth();

        CommandContainer comCont = new CommandContainer("/remove AMD".split("\\s"),
                callbackFlag, chatID, msgID);
        JedisHandler.removeAll(chatID);

        SendMessage messageForUser = Remove.removeAsset(comCont);
        assertEquals( "Should return correct error message for user",
                "Your portfolio is empty. Nothing to remove.", messageForUser.getText());
    }

    @Test
    public void testRemoveAll() {
        JedisHandler.auth();

        CommandContainer comCont = new CommandContainer("/removeAll".split("\\s"), callbackFlag, chatID, msgID);
        assertFalse("Container should not have error", comCont.hasError());
        SendMessage messageForUser = RemoveAll.removeAll(comCont ,inputMessage);
        assertEquals("After removing whole portfolio, it should be empty",
                new HashMap<String, Integer>(), JedisHandler.getUserData(comCont.getChatID()));
        assertEquals("Should return correct message for user",
                "Removed all tickers.", messageForUser.getText());
    }
}
