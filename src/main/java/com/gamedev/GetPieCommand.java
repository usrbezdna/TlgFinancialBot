package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GetPieCommand {
    private static final Logger logger = LoggerFactory.getLogger(GetPieCommand.class);

    public static ReturningValues pie(SendPhoto sendPhoto, String chat_id, Boolean numFlag){
        sendPhoto.setChatId(chat_id);

        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Can't handle current diagram!");
        try {
            Map<String, Integer> userData = JedisHandler.getUserData(chat_id);

            if (userData != null) {
                sendPhoto.setPhoto(
                        new InputFile(new ByteArrayInputStream(Diagram
                                            .createDiagram(userData, numFlag)
                                            .toByteArray()),
                                    "PortfolioDiagram.jpeg")
                );
                sendPhoto.setCaption("Diagram of your assets with their cost.");
            }

        } catch (IOException e){
            logger.error("Error in building diagram", e);
        }
        return new ReturningValues(message, sendPhoto);
    }
}
