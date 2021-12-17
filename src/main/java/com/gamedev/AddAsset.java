package com.gamedev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.HashMap;
import java.util.Map;

public class AddAsset {
    private static final Logger logger = LoggerFactory.getLogger(AddAsset.class);

    public static SendMessage addAsset (CommandContainer comCont){

        SendMessage message = new SendMessage();
        message.setChatId(comCont.getChatID());

        String ticker = comCont.getArgument();
        String amount = comCont.getData();
        int intAmount;
        try {
            if (amount == null) {
                message.setText("Please specify number of assets");
                return message;
            }
            else
                intAmount = Integer.parseInt(amount);
        } catch (NumberFormatException e){
            logger.warn("Couldn't parse " + amount + "as integer, returned error to user");
            message.setText("Please specify correct amount");
            return message;
        }
        String chat_id = comCont.getChatID();
        String errPrice = "Invalid ticker. Please make sure that spelling is correct.";

        if (ticker == null){
            logger.warn("Ticker is not provided");
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