package commands;
import architecture.BasicCommand;
import architecture.CommandContainer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import utils.KeyboardSetUp;
import utils.StockAPI;
import utils.StockNewsAPI;

import java.util.HashMap;
import java.util.List;

public class News extends BasicCommand {

    public static SendMessage getNewsForTicker(CommandContainer comCont, SendMessage message){
        String msgText = String.format("Latest news for ticker %s", comCont.getArgument()) +
                "\n\n" + "===========================\n" +
                StockNewsAPI.getNewsMessage(StockAPI.getCompanyName(comCont.getArgument()));

        message.setReplyMarkup(getMessageKeyboard());


        message.setText(msgText);
        message.setChatId(comCont.getChatID());
        return message;
    }


    private static InlineKeyboardMarkup getMessageKeyboard(){
        List<String> descriptions = StockNewsAPI.getDescriptions();

        InlineKeyboardMarkup keyboard = KeyboardSetUp.setInlineKeyboard(new HashMap<String, String>() {{
            for (int i=0; i< descriptions.size(); i++){
                String descForAudio = descriptions.get(i).replaceAll("\\s", "_");
                put(String.valueOf(i+1), "/audio " + descForAudio);
            }
        }});
        return keyboard;
    }


    @Override
    public int getNumberOfArgs() {
        return 1;
    }

    @Override
    public void validateArgs(CommandContainer comCont) {
        String ticker = comCont.getArgument();

        Double stockPrice = StockAPI.getStockPriceUSD(ticker);
        if (stockPrice == null) {
            comCont.setError("Invalid ticker. Please make sure that spelling is correct.");
        }
    }
}
