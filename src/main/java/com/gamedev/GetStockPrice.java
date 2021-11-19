package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import yahoofinance.*;

public class GetStockPrice {
    public static void getPrice(SendMessage message, BotClass bot,
        String chat_id, ReplyKeyboardMarkup keyboard, String stockTicker) {
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
        bot.sendMessage(message);
    }
}
