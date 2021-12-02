package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.*;
import java.util.Map;

public class GetPieCommand {
    public static SendPhoto pie(SendPhoto sendPhoto, String chat_id){
        sendPhoto.setChatId(chat_id);
        sendPhoto.setCaption("This diagram was made for test. Enjoy!");
        try {
            Map<String, String> userData = JedisHandler.getUserData(chat_id);

            if (userData != null) {
                sendPhoto.setPhoto(
                        new InputFile(new ByteArrayInputStream(DiagramClass
                                .CreateDiagram(userData)
                                .toByteArray()),
                                "TD.jpeg")
                );
            }
            else {
                sendPhoto.setCaption("ERROR");
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        return sendPhoto;
    }
}
