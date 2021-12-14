package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddAssetClass {
    public static SendMessage addAsset (CommandContainer comCont){

        SendMessage message = new SendMessage();
        message.setChatId(comCont.getChatID());

        String ticker = comCont.getArgument();
        String amount = comCont.getData();
        String chat_id = comCont.getChatID();
        String errPrice = "Can`t find current ticker, try again please";

        if (ticker == null || !isNumeric(amount) ){
            message.setText("Can't parse your input");
            return message;
        } else {ticker = ticker.toUpperCase();}

        try{
            Double stockPrice = GetPortfolioClass.getStockPriceUSD(ticker);
            if (stockPrice == null) {
                message.setText(errPrice);
            }  else {
                Map<String, String> userData = JedisHandler.getUserData(chat_id);
                if (userData == null)
                    userData = new HashMap<>();

                userData.merge(ticker, amount,
                        (oldValue, newValue) -> String.valueOf(Integer.parseInt(oldValue) + Integer.parseInt(newValue)));
                JedisHandler.setUserData(chat_id, (HashMap<String, String>) userData);
                message.setText(String.format("Added ticker %s with amount: %s", ticker, amount));
            }
        } catch (Exception ignored){}
        return message;
    }

    private static boolean isNumeric(String amount) {
        return amount.matches("\\d+(\\.\\d+)?");
    }
}