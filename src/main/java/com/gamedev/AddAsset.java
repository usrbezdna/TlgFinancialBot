package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.HashMap;
import java.util.Map;

public class AddAsset {
    public static SendMessage addAsset (CommandContainer comCont){

        SendMessage message = new SendMessage();
        message.setChatId(comCont.getChatID());

        String ticker = comCont.getArgument();
        String amount = comCont.getData();
        int intAmount;
        try{
            if (amount == null) {
                message.setText("Please specify number of assets");
                return message;
            }
            else
                intAmount = Integer.parseInt(amount);
        }catch (NumberFormatException e){
            e.printStackTrace();
            message.setText("Please specify correct amount");
            return message;
        }
        String chat_id = comCont.getChatID();
        String errPrice = "Can`t find current ticker, try again please";

        if (ticker == null){
            message.setText("Can't parse your input");
            return message;
        } else {ticker = ticker.toUpperCase();}


        Double stockPrice = StockAPI.getStockPriceUSD(ticker);
        if (stockPrice == null) {
            message.setText(errPrice);
        } else {
            Map<String, Integer> userData = JedisHandler.getUserData(chat_id);
            if (userData == null)
                userData = new HashMap<>();

            userData.merge(ticker, intAmount, Integer::sum);
            JedisHandler.setUserData(chat_id, userData);
            message.setText(String.format("Added ticker %s with amount: %s", ticker, amount));
        }
        return message;
    }
}