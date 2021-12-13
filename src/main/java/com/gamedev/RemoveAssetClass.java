package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.HashMap;
import java.util.Map;

public class RemoveAssetClass {
    public static SendMessage removeAsset(String[] args, String chat_id){

        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        String ticker = args[0];
        String errPrice = "Can`t find current ticker, try again please";

        if (ticker != null){
            try {
                Map<String, String> userData = JedisHandler.getUserData(chat_id);
                if (userData == null) {
                    message.setText("Your portfolio is empty. Nothing to remove.");
                } else{
                    if (!userData.containsKey(ticker)){
                        message.setText("There's no such ticker in your portfolio.");
                    }
                    userData.remove(ticker);
                    JedisHandler.setUserData(chat_id, (HashMap<String, String>) userData);
                }
            } catch (Exception ignored) {}
            message.setText(String.format("Removed ticker %s", ticker));
        }
        else {message.setText(errPrice);}
        return message;
    }
}
