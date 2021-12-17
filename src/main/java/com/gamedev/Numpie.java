package com.gamedev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

public class Numpie {
    public static int numberOfArgs = 0;
    private static final Logger logger = LoggerFactory.getLogger(Numpie.class);
    public static ReturningValues pie(SendPhoto sendPhoto, String chat_id){
        sendPhoto.setChatId(chat_id);

        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Can't handle current diagram!");
        try {
            Map<String, Integer> userData = JedisHandler.getUserData(chat_id);
            if (userData != null) {
                sendPhoto.setPhoto(
                        new InputFile(new ByteArrayInputStream(Diagram
                                .createDiagram(userData, true)
                                .toByteArray()),
                                "PortfolioDiagram.jpeg")
                );
                sendPhoto.setCaption("Diagram of your assets with their amount.");
            }
        } catch (IOException e){
            logger.error("Error in building diagram", e);
        }
        return new ReturningValues(message, sendPhoto);
    }
}
