package commands;

import architecture.BasicCommand;
import architecture.CommandContainer;
import utils.JedisHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class RemoveAll extends BasicCommand {
    public static SendMessage removeAll(CommandContainer comCont, SendMessage message){
        JedisHandler.removeAll(comCont.getChatID(), comCont.getDataBase());
        message.setChatId(comCont.getChatID());
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
