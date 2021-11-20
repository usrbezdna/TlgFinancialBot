import org.junit.*;
import static org.junit.Assert.*;
import com.gamedev.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;


public class TestHashmapFuncs {

    private SendMessage inputMessage;
    private final Long chatID = 1337L;
    private final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();


    @Before
    public void initialize(){

        inputMessage = new SendMessage();
        inputMessage.setChatId(chatID.toString());
    }

    @Test
    public void testStartCommand(){
        String command = "/start";
        inputMessage.setText(command);

        String realText = StartCommand.start(inputMessage, chatID.toString(), keyboard).getText();
        String expectedText = "Started! Try \"/help\" for commands.";

        assertEquals(realText, expectedText);
    }




}
