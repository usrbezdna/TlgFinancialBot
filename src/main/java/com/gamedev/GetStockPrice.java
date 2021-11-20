package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import yahoofinance.*;

public class GetStockPrice {
    public static SendMessage getPrice(SendMessage message, String chat_id, String stockTicker) {
        message.setChatId(chat_id);
        String errPrice = "Can`t find current ticker, try again please";
        try {
             String stockPrice = YahooFinance.get(stockTicker).toString();
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
