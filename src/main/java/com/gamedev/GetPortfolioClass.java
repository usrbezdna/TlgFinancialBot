package com.gamedev;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class GetPortfolioClass {

    public static Double getStockPriceUSD(String stockName) throws IOException {
        Stock stockObj = YahooFinance.get(stockName);
        Double currencyRatio;
        if (stockObj.getCurrency() == "USD") {
            currencyRatio = 1.0;
        } else {
            currencyRatio = YahooFinance.
                                    get(stockObj.getCurrency() + "=X").
                                    getQuote().
                                    getPrice().
                                    doubleValue();
        }
        return stockObj.getQuote().getPrice().doubleValue() / currencyRatio;
    }

    public static HashMap<String, Double> calcPortfolio(Map<String, String> rawPortfolio) {
        HashMap<String, Double> processedPortfolio = new HashMap<String, Double>();
        for (Map.Entry<String, String> stock : rawPortfolio.entrySet()) {
            try {
                processedPortfolio.put(stock.getKey(), 
                    Integer.parseInt(stock.getValue()) * getStockPriceUSD(stock.getKey()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processedPortfolio;
    }

    public static SendMessage calcPortfolioBalance(SendMessage message, 
                                                        String chat_id, 
                                                        Map<String, String> portfolio, 
                                                        Boolean hasCallback) {
        Double balance = 0.0;
        String details = "";
        message.setChatId(chat_id);

        if (portfolio == null) {
            message.setText(balance.toString());
            return message;
        }
        HashMap<String, Double> calculated = calcPortfolio(portfolio);
        for (Map.Entry<String, Double> stock : calculated.entrySet()) {
            balance += stock.getValue();
            if (hasCallback)
                details += stock.getKey() + " - " + stock.getValue().toString() + "\n";
        }
        Double result = Math.floor(balance);
        if (hasCallback) {
            details = "$" + result.toString() + "\n" + details;
            message.setText(details);
        } else {
            message.setText("$" + result.toString());
        }
        return message;
    }
}