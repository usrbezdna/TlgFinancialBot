package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.File;

public class GetPieCommand {
    public static void pie(SendPhoto sendPhoto, BotClass bot,
                           String chat_id, ReplyKeyboardMarkup keyboard){
        DiagramClass.CreateDiagram();
        sendPhoto.setChatId(chat_id);
        sendPhoto.setCaption("This diagram was made for test. Enjoy!");
        sendPhoto.setPhoto(new InputFile(new File("TD.jpeg")));
        bot.sendPhoto(sendPhoto);
    }
}
