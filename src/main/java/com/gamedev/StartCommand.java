package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class StartCommand {
    public static void start(SendMessage message, BotClass bot,
                             String chat_id, ReplyKeyboardMarkup keyboard) {
        message.setChatId(chat_id);
        message.setText("Started! Try \"/help\" for commands.");
        message.setReplyMarkup(keyboard);
        bot.sendMessage(message);
    }
}
