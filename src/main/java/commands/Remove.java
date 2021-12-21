package commands;

import architecture.BasicCommand;
import architecture.CommandContainer;
import redis.clients.jedis.Jedis;
import utils.JedisHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;


public class Remove extends BasicCommand {
    public static SendMessage removeAsset(CommandContainer comCont){

        SendMessage message = new SendMessage();
        String chat_id = comCont.getChatID();
        Jedis dataBase = comCont.getDataBase();

        message.setChatId(chat_id);
        String ticker = comCont.getArgument().toUpperCase();

        Map<String, Integer> userData = JedisHandler.getUserData(chat_id, dataBase);
        if (userData == null || userData.isEmpty()) {
            message.setText("Your portfolio is empty. Nothing to remove.");
        } else {
            if (!userData.containsKey(ticker)) {
                message.setText("There's no such ticker in your portfolio.");
            } else {
                userData.remove(ticker);
                JedisHandler.setUserData(chat_id, userData, dataBase);
                message.setText(String.format("Removed ticker %s", ticker));
            }
        } return message;
    }

    @Override
    public int getNumberOfArgs() {
        return 1;
    }

    @Override
    public void validateArgs(CommandContainer comCont) {}
}
