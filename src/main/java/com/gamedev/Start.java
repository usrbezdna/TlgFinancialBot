package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class Start {
    public static int numberOfArgs = 0;
    public static SendMessage start(SendMessage message, String chat_id, ReplyKeyboardMarkup keyboard) {
        message.setChatId(chat_id);
        message.setText("Started! Try \"/help\" for commands.");
        message.setReplyMarkup(keyboard);
        return message;
    }
}
