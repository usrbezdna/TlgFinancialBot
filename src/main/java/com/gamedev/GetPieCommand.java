package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.*;
import java.util.Map;

public class GetPieCommand {
    public static ReturningValues pie(SendPhoto sendPhoto, String chat_id){

        sendPhoto.setChatId(chat_id);

        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Can't handle current diagram!");


        try {
            Map<String, String> userData = JedisHandler.getUserData(chat_id);

            if (userData != null) {
                sendPhoto.setPhoto(
                        new InputFile(new ByteArrayInputStream(DiagramClass
                                .CreateDiagram(userData)
                                .toByteArray()),
                                "PortfolioDiagram.jpeg")
                );
                sendPhoto.setCaption("This diagram was made for test. Enjoy!");
            }

        } catch (IOException e){
            e.printStackTrace();

        }

        return new ReturningValues(message, sendPhoto);
    }
}
