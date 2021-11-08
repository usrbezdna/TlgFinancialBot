import jdk.jshell.spi.ExecutionControl;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CommandExecutorClass {
    public static String executeCommand(String msg){
        return msg;
    }
}
