package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandExecutorClass {

    private static final SendMessage message = new SendMessage();
    private static final SendPhoto sendPhoto = new SendPhoto();
    private static final ReplyKeyboardMarkup keyboard = setUpKeyboard();
    private static final BotClass bot = Main.getBot();
    private static String chat_id = "";

    public static void setChat_id(Update update){
        chat_id = String.valueOf(update.getMessage().getChatId());
    }

    public static void start(){
        message.setChatId(chat_id);
        message.setText("Started! Try \"/help\" for commands.");
        message.setReplyMarkup(keyboard);
        bot.sendMessage(message);
    }

    public static void help(){
        message.setChatId(chat_id);
        message.setText("This is help. We have \"/pie\" - makes a test " +
        "pie diagram, \"/help\" - helps and \"/start\" - starts and opens " +
        "inline keyboard. Try them all :)");
        bot.sendMessage(message);
    }

    public static void pie(){
        DiagramClass.CreateDiagram();
        sendPhoto.setChatId(chat_id);
        sendPhoto.setCaption("This diagram was made for test. Enjoy!");
        sendPhoto.setPhoto(new InputFile(new File("TD.jpeg")));
        bot.sendPhoto(sendPhoto);
    }

    private static ReplyKeyboardMarkup setUpKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/help"); //from hashmap
        row.add("/pie");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
