package com.gamedev;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class GetPortfolioClass {

    public static Stock getStock(String stockName) throws IOException {
        return YahooFinance.get(stockName);
    }

    public static HashMap<String, Double> calcPortfolio(Map<String, String> rawPortfolio) {
        HashMap<String, Double> processedPortfolio = new HashMap<String, Double>();
        for (Map.Entry<String, String> stock : rawPortfolio.entrySet()) {
            try {
                processedPortfolio.put(stock.getKey(), Integer.parseInt(stock.getValue()) *
                        getStock(stock.getKey()).
                                getQuote().
                                getPrice().
                                doubleValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processedPortfolio;
    }

    public static SendMessage calcPortfolioBalance(SendMessage message, String chat_id, Map<String, String> portfolio) {
        Double balance = 0.0;
        message.setChatId(chat_id);
        HashMap<String, Double> calculated = calcPortfolio(portfolio);
        for (Map.Entry<String, Double> stock : calculated.entrySet()) {
            balance += stock.getValue();
        }
        Double result = Math.floor(balance);
        message.setText(result.toString());
        return message;
    }
}