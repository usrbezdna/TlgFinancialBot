package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;


public class CommandExecutor {

    private static SendMessage message = new SendMessage();
    private static SendPhoto sendPhoto = new SendPhoto();
    private static EditMessageText edited_message = new EditMessageText();
    private static final ReplyKeyboardMarkup keyboard = KeyboardSetUp.setReplyKeyboard();
    private static final Bot bot = Main.getBot();
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
        SendMessage msg = Start.start(message, chat_id, keyboard);
        bot.sendEverything(msg);
        releaseFields();
    }

    public static void help(){
        SendMessage msg = Help.help(message, chat_id);
        bot.sendEverything(msg);
        releaseFields();
    }

    public static void add(CommandContainer comCont){
        SendMessage msg = Add.addAsset(comCont);
        bot.sendEverything(msg);
        releaseFields();
    }

    public static void remove(CommandContainer comCont){
        SendMessage msg = Remove.removeAsset(comCont);
        bot.sendEverything(msg);
        releaseFields();
    }

    public static void price(CommandContainer comCont)  {
        SendMessage msg = Price.getPrice(comCont);
        bot.sendEverything(msg);
    }

    public static void pie(Boolean numFlag) {
        ReturningValues pieStatus = Pie.pie(sendPhoto, chat_id, numFlag);
        if (pieStatus._photo_.getCaption() == null) bot.sendEverything(pieStatus._message_);
        else bot.sendEverything(pieStatus._photo_);
        releaseFields();
    }

    public static void balance(CommandContainer comCont){
        ReturningValues balanceStatus = Balance.getBalance(comCont, message, edited_message, chat_id);
        if (balanceStatus._message_.getText() == null) bot.sendEverything(balanceStatus._edited_message_);
        else bot.sendEverything(balanceStatus._message_);
        releaseFields();
    }

    public static void removeAll(){
        SendMessage removeAllStatus = RemoveAll.removeAll(chat_id);
        bot.sendEverything(removeAllStatus);
        releaseFields();
    }

    public static void printError(CommandContainer comCont){
        message.setText(comCont.getErrorMessage());
        message.setChatId(chat_id);
        bot.sendEverything(message);
        releaseFields();
    }

    public static void getNews(CommandContainer comCont) {
        ReturningValues msg = News.news(comCont, message, edited_message);
        if (msg._message_.getText() == null) bot.sendEverything(msg._edited_message_);
        else bot.sendEverything(msg._message_);
        releaseFields();
    }
}
