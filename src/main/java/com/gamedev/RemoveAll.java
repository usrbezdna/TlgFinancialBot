package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class RemoveAll {
    public static int numberOfArgs = 0;
    public static SendMessage removeAll(String chatID){
        SendMessage message = new SendMessage();
        JedisHandler.removeAll(chatID);
        message.setText("Removed all tickers.");
        return message;
    }
}
