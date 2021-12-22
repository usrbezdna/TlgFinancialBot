import architecture.CommandContainer;
import commands.News;
import commands.PortfolioNews;
import commands.RemoveAll;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Testcontainers
@RunWith(JUnit4.class)
public class TestNews {
    private final SendMessage inputMessage = new SendMessage();
    private final String chatID = "1337";
    private final String msgID = "7331";
    private final boolean callbackFlag = false;
    private static Jedis testDataBase;

    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:3-alpine")
            .withExposedPorts(6379);

    @BeforeClass
    public static void setUp() {
        redisContainer.start();
        String contHost = redisContainer.getHost();
        Integer contPort = redisContainer.getMappedPort(6379);
        testDataBase = new Jedis(contHost, contPort);
    }


    @Test
    public void testNewsIncorrectTicker(){
        CommandContainer incorrect = new CommandContainer("/news ZXC".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        new News().validateArgs(incorrect);
        assertTrue("Container should have error", incorrect.hasError());
        assertEquals("Should send correct error message",
                "Invalid ticker. Please make sure that spelling is correct.", incorrect.getErrorMessage());
    }

    @Test
    public void testEmptyPortfolio(){
        CommandContainer empty = new CommandContainer("/portfolioNews".split("\\s"),
                callbackFlag, chatID, msgID, testDataBase);
        RemoveAll.removeAll(empty, inputMessage);
        new PortfolioNews().validateArgs(empty);
        assertTrue("Container should have error", empty.hasError());
        assertEquals("Should send correct error message",
                "Your portfolio is empty", empty.getErrorMessage());
    }
}
