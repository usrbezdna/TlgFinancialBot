package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class HelpCommand {
    public static SendMessage help(SendMessage message, String chat_id) {
        message.setChatId(chat_id);
        message.setText("This is help. We have \"/pie\" - makes a test " +
                "pie diagram, \"/help\" - helps and \"/start\" - starts and opens " +
                "inline keyboard. And also you can get a price of asset, just type:" +
                " \"/price AAPL\". Try them all :)");
        return message;
    }
}
