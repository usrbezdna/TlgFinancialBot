package commands;

import architecture.BasicCommand;
import architecture.CommandContainer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class Start extends BasicCommand {

    public static SendMessage start(SendMessage message, String chat_id, ReplyKeyboardMarkup keyboard) {
        message.setChatId(chat_id);
        message.setText("Started! Try \"/help\" for commands.");
        message.setReplyMarkup(keyboard);
        return message;
    }

    @Override
    public int getNumberOfArgs() {
        return 0;
    }

    @Override
    public void validateArgs(CommandContainer comCont) {}
}
