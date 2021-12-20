package commands;
import architecture.BasicCommand;
import architecture.CommandContainer;
import architecture.ReturningValues;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import utils.KeyboardSetUp;
import utils.StockAPI;
import utils.StockNewsAPI;

import java.util.HashMap;
import java.util.List;

public class News extends BasicCommand {

    public static ReturningValues getNewsForTicker(CommandContainer comCont, SendMessage message){
        if (comCont.hasCallback() && comCont.getArgument().startsWith("+++++")) {
            String description = comCont.getArgument().substring(5);
            SendVoice audioDescription = Audio.returnAudio(description.replaceAll("_", "\\s"));
            audioDescription.setChatId(comCont.getChatID());
            return new ReturningValues(audioDescription); 
        }

        String delimiter = "===========================\n";
        String ticker = comCont.getArgument();
        String msgText = String.format("Latest news for ticker %s", ticker) +
                    "\n\n" + delimiter +
                    StockNewsAPI.getNewsMessage(StockAPI.getCompanyName(ticker));

        List<String> descriptions = StockNewsAPI.getDesctiptions();

        InlineKeyboardMarkup keyboard = KeyboardSetUp.setInlineKeyboard(new HashMap<String, String>() {{
            for (int i = 0; i < descriptions.size(); i++){
                put(String.valueOf(i+1), "/news " + "+++++" + descriptions.get(i));
            }
        }});

        message.setReplyMarkup(keyboard);
        message.setText(msgText);
        message.setChatId(comCont.getChatID());
        return new ReturningValues(message);
    }

    @Override
    public int getNumberOfArgs() {
        return 1;
    }

    @Override
    public void validateArgs(CommandContainer comCont) {
        String ticker = comCont.getArgument();

        if (!ticker.startsWith("+++++")){
            Double stockPrice = StockAPI.getStockPriceUSD(ticker);
            if (stockPrice == null) {
                comCont.setError("Invalid ticker. Please make sure that spelling is correct.");
            }
        }
    }
}
