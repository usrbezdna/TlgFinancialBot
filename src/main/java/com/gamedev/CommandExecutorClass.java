package com.gamedev;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

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
       System.out.println(photo.getPhoto());
       if (photo.getPhoto() == null) {
           message.setText("Can't handle");
           message.setChatId(chat_id);
           bot.sendMessage(message);
       } else{
           bot.sendPhoto(photo);
       }
    }

    public static void price(String[] stockTicker)  {
        System.out.println(stockTicker[0]);
        SendMessage msg = GetStockPrice.getPrice(message, chat_id, stockTicker[0]);
        bot.sendMessage(msg);
    }

    public static void balance(String[] args){
        SendMessage msg = null;
        for (String arg : args) 
            System.out.println("Argument: " + arg);
        try {
            if (args[0] == "Callback") {
                System.out.println("Callback!!!");
                int message_id = toIntExact(Long.parseLong(args[2]));
                if (args[1].equals("/balance__show__assets")) {
                    msg = GetPortfolioClass.calcPortfolioBalance(message, chat_id, JedisHandler.getUserData(chat_id), true);
                    EditMessageText new_message = new EditMessageText();
                    new_message.setChatId(chat_id);
                    new_message.setMessageId(message_id);
                    new_message.setText(msg.getText().toString());
                    bot.updateMessage(new_message);
                }
            } else {
                msg = GetPortfolioClass.calcPortfolioBalance(message, chat_id, JedisHandler.getUserData(chat_id), false);
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText("Show detailed portfolio");
                button.setCallbackData("/balance__show__assets");
                rowInline.add(button);
                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                msg.setReplyMarkup(markupInline);
                bot.sendMessage(msg);
            }            
        } catch (Exception ignored){}
    }

    public static void add(String[] args){
        SendMessage msg = AddAssetClass.addAsset(args, chat_id);
        bot.sendMessage(msg);
    }
}
