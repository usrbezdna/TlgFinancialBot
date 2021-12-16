package com.gamedev;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class GetPortfolio {



    public static HashMap<String, Double> calcPortfolio(Map<String, Integer> rawPortfolio) {
        HashMap<String, Double> processedPortfolio = new HashMap<>();

        for (Map.Entry<String, Integer> stock : rawPortfolio.entrySet()) {
            //На вход в базу идет валидация тикера и количества => NullPointer невозможен
            processedPortfolio.put(stock.getKey(), stock.getValue() * StockAPI.getStockPriceUSD(stock.getKey()));
        }
        return processedPortfolio;
    }

    public static SendMessage calcPortfolioBalance(SendMessage message, String chat_id, Map<String,
                                                    Integer> portfolio, Boolean hasCallback)
    {
        Double balance = 0.0;  StringBuilder details = new StringBuilder(); message.setChatId(chat_id);

        if (portfolio == null) {
            message.setText(balance.toString());
            return message;
        }

        HashMap<String, Double> calculated = calcPortfolio(portfolio);

        for (Map.Entry<String, Double> stock : calculated.entrySet()) {
            balance += stock.getValue();
            if (hasCallback)
                details.append(stock.getKey())
                        .append(" - ")
                        .append(Math.round(stock.getValue() * 100.0) / 100.0)
                        .append("\n");
        }

        double result = Math.round(balance * 100.0) / 100.0;

        if (hasCallback) {
            details.insert(0, "$" + result + "\n");
            message.setText(details.toString());
        } else {
            message.setText("$" + result);
        }
        return message;
    }
}