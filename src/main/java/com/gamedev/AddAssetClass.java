package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.HashMap;
import java.util.Map;

public class AddAssetClass {
    public static SendMessage addAsset (String[] args, String chat_id){

        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        String ticker = args[0]; String amount = args[1];
        String errPrice = "Can`t find current ticker, try again please";

        if (args.length != 2 | !isNumeric(args[1]) ){
            message.setText("Can't parse your");
            return message;
        }

        try{
            String stockPrice = GetPortfolioClass.getStockPriceUSD(args[0]).toString();
            if (stockPrice.contains("null")) {
                message.setText(errPrice);
            } else {
                Map<String, String> userData = JedisHandler.getUserData(chat_id);
                if (userData == null) {
                    userData = new HashMap<String, String>();
                }
                userData.merge(ticker, amount,
                        (oldValue, newValue) -> String.valueOf(Integer.parseInt(oldValue) + Integer.parseInt(newValue)));
                JedisHandler.setUserData(chat_id, (HashMap<String, String>) userData);
                message.setText(String.format("Added ticker %s with amount %s", ticker, amount));
            }
        } catch (Exception ignored){}
        return message;
    }

    private static boolean isNumeric(String amount) {
        return amount.matches("\\d+(\\.\\d+)?");
    }
}