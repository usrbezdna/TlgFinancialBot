package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutorClass {

    private static final SendMessage message = new SendMessage();
    private static final SendPhoto sendPhoto = new SendPhoto();
    private static final ReplyKeyboardMarkup keyboard = setUpKeyboard();
    private static final BotClass bot = Main.getBot();
    private static String chat_id = "";

    public static void updateChatID(Update update){
        chat_id = String.valueOf(update.getMessage().getChatId());
    }

    public static void start() {
        StartCommand.start(message, bot, chat_id, keyboard);
    }

    public static void help(){
        HelpCommand.help(message, bot, chat_id);
    }

    public static void pie(){
        GetPieCommand.pie(sendPhoto, bot, chat_id, keyboard);
    }

    public static void price (String stockTicker)  {
        GetStockPrice.getPrice(message, bot, chat_id, keyboard, stockTicker);
    }

    private static ReplyKeyboardMarkup setUpKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/help"); row.add("/pie"); //from hashmap
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
