package commands;

import architecture.BasicCommand;
import architecture.CommandContainer;
import utils.JedisHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class RemoveAll extends BasicCommand {
    public static SendMessage removeAll(String chatID){
        SendMessage message = new SendMessage();
        JedisHandler.removeAll(chatID);
        message.setText("Removed all tickers.");
        return message;
    }

    @Override
    public int getNumberOfArgs() {
        return 0;
    }

    @Override
    public void validateArgs(CommandContainer comCont) {}
}
