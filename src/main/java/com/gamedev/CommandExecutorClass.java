package com.gamedev;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class CommandExecutorClass {

    private static final SendMessage message = new SendMessage();
    private static final SendPhoto sendPhoto = new SendPhoto();
    private static final ReplyKeyboardMarkup keyboard = KeyboardSetUpClass.setKeyboard();
    private static final BotClass bot = Main.getBot();
    private static String chat_id = "";

    public static void updateChatID(Update update){
        chat_id = String.valueOf(update.getMessage().getChatId());
    }

    public static void start() {
        SendMessage msg = StartCommand.start(message, chat_id, keyboard);
        bot.sendMessage(msg);
    }

    public static void help(){
        SendMessage msg = HelpCommand.help(message, chat_id);
        bot.sendMessage(msg);
    }

    public static void pie(){
       SendPhoto photo = GetPieCommand.pie(sendPhoto, chat_id);
       bot.sendPhoto(photo);
    }

    public static void price (String stockTicker)  {
        SendMessage msg = GetStockPrice.getPrice(message, chat_id, stockTicker);
        bot.sendMessage(msg);
    }

    public static void balance (){
        try {
            SendMessage msg = GetPortfolioClass.calcPortfolioBalance(message, chat_id, JedisHandler.getUserData("234"));
            bot.sendMessage(msg);
        } catch (Exception ignored){}
    }
}
