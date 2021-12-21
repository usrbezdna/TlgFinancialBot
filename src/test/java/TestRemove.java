import architecture.CommandContainer;
import commands.Add;
import commands.Remove;
import commands.RemoveAll;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.clients.jedis.Jedis;
import utils.JedisHandler;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@Testcontainers
@RunWith(JUnit4.class)
public class TestRemove {
    private final SendMessage inputMessage = new SendMessage();
    private final String chatID = "1337";
    private final String msgID = "7331";
    private final boolean callbackFlag = false;


    private static Jedis testDataBase;

    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:3-alpine")
            .withExposedPorts(6379).withReuse(true);

    @BeforeClass
    public static void setUp() {
        redisContainer.start();

        String contHost = redisContainer.getHost();
        Integer contPort = redisContainer.getMappedPort(6379);

        testDataBase = new Jedis(contHost, contPort);
    }


    @Test
    public void testRemoveCorrect(){

        CommandContainer comCont = new CommandContainer("/remove AMD".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        new Remove().validateArgs(comCont);

        assertFalse("Container should not have error", comCont.hasError());

        Map<String, Integer> before = JedisHandler.getUserData(chatID, testDataBase);
        Add.addAsset(new CommandContainer("/add AMD 30".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase));
        SendMessage messageForUser = Remove.removeAsset(comCont);
        Map<String, Integer> after = JedisHandler.getUserData(chatID, testDataBase);

        assertEquals( "Message should contain correct text",
                "Removed ticker AMD", messageForUser.getText());
        assertEquals("User portfolio should be equal after combination \"add + remove\"", before, after);
    }

    @Test
    public void testRemoveWithoutArgument() {
        CommandContainer withoutArgs = new CommandContainer("/remove".split("\\s"), callbackFlag, chatID, msgID, testDataBase);
        assertTrue("Container should have error", withoutArgs.hasError());
        assertEquals("Should return correct error message",
                "Incorrect number of arguments, try \"/help\"", withoutArgs.getErrorMessage());
    }

    @Test
    public void testRemoveIncorrectArgument(){

        CommandContainer comCont = new CommandContainer("/remove ZXCghoul".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        Add.addAsset(new CommandContainer("/add AMD 15".split("\\s"), callbackFlag, chatID, msgID, testDataBase));
        SendMessage messageForUser = Remove.removeAsset(comCont);
        assertEquals("Should return correct error message for user",
                "There's no such ticker in your portfolio.", messageForUser.getText());
    }

    @Test
    public void testRemoveEmpty(){

        CommandContainer comCont = new CommandContainer("/remove AMD".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        JedisHandler.removeAll(chatID, testDataBase);

        SendMessage messageForUser = Remove.removeAsset(comCont);
        assertEquals( "Should return correct error message for user",
                "Your portfolio is empty. Nothing to remove.", messageForUser.getText());
    }

    @Test
    public void testRemoveAll() {

        CommandContainer comCont = new CommandContainer("/removeAll".split("\\s"), callbackFlag, chatID, msgID, testDataBase);
        assertFalse("Container should not have error", comCont.hasError());
        SendMessage messageForUser = RemoveAll.removeAll(comCont ,inputMessage);
        assertEquals("After removing whole portfolio, it should be empty",
                new HashMap<String, Integer>(), JedisHandler.getUserData(comCont.getChatID(), testDataBase));
        assertEquals("Should return correct message for user",
                "Removed all tickers.", messageForUser.getText());
    }
}
