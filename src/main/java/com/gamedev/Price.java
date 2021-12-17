package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


public class Price {
    public static int numberOfArgs = 2;
    public static SendMessage getPrice(CommandContainer comCont) {
        SendMessage message = new SendMessage();
        message.setChatId(comCont.getChatID());
        String errPrice = "Invalid ticker. Please make sure that spelling is correct.";

        Double stockPrice = StockAPI.getStockPriceUSD(comCont.getArgument());
        if (stockPrice == null)
            message.setText(errPrice);
        else message.setText(String.format("Found ticker with price %s", stockPrice));

        return message;
    }
}
