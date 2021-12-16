package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RemoveAsset {
    private static final Logger logger = LoggerFactory.getLogger(RemoveAsset.class);

    public static SendMessage removeAsset(CommandContainer comCont){

        SendMessage message = new SendMessage();
        String chat_id = comCont.getChatID();

        message.setChatId(chat_id);
        String ticker = comCont.getArgument();
        String errMsg = "Invalid command";

        if (ticker != null){
            ticker = ticker.toUpperCase();
            try {
                Map<String, Integer> userData = JedisHandler.getUserData(chat_id);
                if (userData == null) {
                    message.setText("Your portfolio is empty. Nothing to remove.");
                } else{
                    if (!userData.containsKey(ticker)){
                        message.setText("There's no such ticker in your portfolio.");
                    }else {
                        userData.remove(ticker);
                        JedisHandler.setUserData(chat_id, userData);
                        message.setText(String.format("Removed ticker %s", ticker));
                    }
                }
            } catch (Exception e) { logger.error("Error in removing asset", e); }

        }
        else {message.setText(errMsg);}
        return message;
    }
}
