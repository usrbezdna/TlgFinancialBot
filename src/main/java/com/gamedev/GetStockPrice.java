package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import yahoofinance.*;

public class GetStockPrice {
    public static SendMessage getPrice(CommandContainer comCont) {
        SendMessage message = new SendMessage();
        message.setChatId(comCont.getChatID());
        String errPrice = "Can`t find current ticker, try again please";

        try {
             String stockPrice = YahooFinance.get(comCont.getArgument()).toString();
             if (stockPrice.contains("null")) {
                 message.setText(errPrice);
             } else {
                 message.setText(String.format("Found ticker with price %s", stockPrice));
             }
        } catch (Exception e) {
            message.setText(errPrice);
            e.printStackTrace();
        }
        return message;
    }
}
