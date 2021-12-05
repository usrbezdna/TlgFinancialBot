package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.HashMap;
import java.util.Objects;

import static java.lang.Math.toIntExact;

public class CommandExecutorClass {

    private static SendMessage message = new SendMessage();
    private static SendPhoto sendPhoto = new SendPhoto();
    private static EditMessageText edited_message = new EditMessageText();
    private static final ReplyKeyboardMarkup keyboard = KeyboardSetUpClass.setReplyKeyboard();
    private static final BotClass bot = Main.getBot();
    private static String chat_id = "";

    public static void updateChatID(Update update){
        chat_id = String.valueOf(update.getMessage().getChatId());
    }

    private static void releaseFields(){
        message = new SendMessage();
        sendPhoto = new SendPhoto();
        edited_message = new EditMessageText();
    }

    public static void start(){
        SendMessage msg = StartCommand.start(message, chat_id, keyboard);
        bot.sendEverything(msg);
        releaseFields();
    }

    public static void help(){
        SendMessage msg = HelpCommand.help(message, chat_id);
        bot.sendEverything(msg);
        releaseFields();
    }

    public static void add(String[] args){
        SendMessage msg = AddAssetClass.addAsset(args, chat_id);
        bot.sendEverything(msg);
        releaseFields();
    }

    public static void price(String[] stockTicker)  {
        SendMessage msg = GetStockPrice.getPrice(message, chat_id, stockTicker[0]);
        bot.sendEverything(msg);
    }

    public static void pie() {
        ReturningValues pieStatus = GetPieCommand.pie(sendPhoto, chat_id);
        if (pieStatus._photo_.getCaption() == null) bot.sendEverything(pieStatus._message_);
        else bot.sendEverything(pieStatus._photo_);
        releaseFields();
    }

    public static void balance(String[] args){
        SendMessage msg;
        if (Objects.equals(args[0], "Callback")) {
        int message_id = toIntExact(Long.parseLong(args[2]));
            if (args[1].equals("detailed")) {

                    msg = GetPortfolioClass
                            .calcPortfolioBalance(message, chat_id, JedisHandler
                                         .getUserData(chat_id), true);

                    edited_message.setChatId(chat_id);
                    edited_message.setMessageId(message_id);
                    edited_message.setText(msg.getText());
                    bot.sendEverything(edited_message);
                }
            }


            else {
                     msg = GetPortfolioClass
                        .calcPortfolioBalance(message, chat_id, JedisHandler
                                .getUserData(chat_id), false);

                InlineKeyboardMarkup keyboard = KeyboardSetUpClass.setInlineKeyboard(new HashMap<String, String>() {{
                    put("Show detailed portfolio", "/balance detailed");
                    put("Show hello message", "/help");
                    put("Some text", "fasfa");
                }});

                msg.setReplyMarkup(keyboard);
                bot.sendEverything(msg);
            }
        releaseFields();
    }
}
