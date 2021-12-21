package commands;

import architecture.BasicCommand;
import architecture.CommandContainer;
import architecture.ReturningValues;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Diagram;
import utils.JedisHandler;


public class Pie extends BasicCommand {
    private static final Logger logger = LoggerFactory.getLogger(Pie.class);

    public static ReturningValues pie(SendPhoto sendPhoto, String chat_id, Boolean numFlag){

        sendPhoto.setChatId(chat_id);
        String typeOfDiagram = numFlag ? "amount" : "cost";

        try {
            Map<String, Integer> userData = JedisHandler.getUserData(chat_id);

            sendPhoto.setPhoto(
                    new InputFile(new ByteArrayInputStream(Diagram
                                        .createDiagram(userData, numFlag)
                                        .toByteArray()),
                                "PortfolioDiagram.jpeg")
            );
            sendPhoto.setCaption(String.format("Diagram of your assets with their %s.", typeOfDiagram));

        } catch (IOException e) {
            logger.error("Error in building diagram", e);
        }
        return new ReturningValues(sendPhoto);
    }

    @Override
    public int getNumberOfArgs() {
        return 0;
    }

    @Override
    public void validateArgs(CommandContainer comCont) {
        Map<String, Integer> userData = JedisHandler.getUserData(comCont.getChatID());
        if (userData == null || userData.isEmpty()){
            comCont.setError("Portfolio is empty, we could not create your diagram");
        }
    }
}
