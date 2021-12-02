package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.*;

public class GetPieCommand {
    public static SendPhoto pie(SendPhoto sendPhoto, String chat_id){
        sendPhoto.setChatId(chat_id);
        sendPhoto.setCaption("This diagram was made for test. Enjoy!");
        try {
            System.out.println(JedisHandler.getUserData("234"));
            sendPhoto.setPhoto(
                    new InputFile(new ByteArrayInputStream(DiagramClass.CreateDiagram(JedisHandler.getUserData("234")).toByteArray()),
                    "TD.jpeg")
            );
        }catch (IOException e){
            e.printStackTrace();
        }
        return sendPhoto;
    }
}
